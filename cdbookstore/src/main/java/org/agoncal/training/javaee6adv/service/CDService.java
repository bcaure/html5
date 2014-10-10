package org.agoncal.training.javaee6adv.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.agoncal.training.javaee6adv.model.CD;
import org.agoncal.training.javaee6adv.model.Genre;
import org.agoncal.training.javaee6adv.model.MajorLabel;

@Stateless
@LocalBean
public class CDService extends AbstractService<CD> implements Serializable {

	private static final long serialVersionUID = -1L;

	public CDService() {
		super(CD.class);
	}

	@Override
	protected Predicate[] getSearchPredicates(Root<CD> root, CD example) {

		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String title = example.getTitle();
		if (title != null && !"".equals(title)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("title")),
					'%' + title.toLowerCase() + '%'));
		}
		String description	 = example.getDescription();
		if (description != null && !"".equals(description)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("description")),
					'%' + description.toLowerCase() + '%'));
		}
		String imageURL = example.getImageURL();
		if (imageURL != null && !"".equals(imageURL)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("imageURL")),
					'%' + imageURL.toLowerCase() + '%'));
		}
		MajorLabel label = example.getLabel();
		if (label != null) {
			predicatesList.add(builder.equal(root.get("label"), label));
		}
		Genre genre = example.getGenre();
		if (genre != null) {
			predicatesList.add(builder.equal(root.get("genre"), genre));
		}
	      return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}
	
	  public CD findByIdWithRelations(Long id) {

          TypedQuery<CD> findByIdQuery = getEntityManager().createQuery(

                       "SELECT DISTINCT c FROM CD c LEFT JOIN FETCH c.label LEFT JOIN FETCH c.genre LEFT JOIN FETCH c.musicians WHERE c.id = :entityId ORDER BY c.id", CD.class);

          findByIdQuery.setParameter("entityId", id);

          CD entity;

          try {

                 entity = findByIdQuery.getSingleResult();

          } catch (NoResultException nre) {

                 entity = null;

          }



          return entity;

    }



    public List<CD> listAllWithRelations() {

          return listAllWithRelations(null, null);

    }



    public List<CD> listAllWithRelations(Integer startPosition, Integer maxResult) {

          TypedQuery<CD> findAllQuery = getEntityManager().createQuery(

                       "SELECT DISTINCT c FROM CD c LEFT JOIN FETCH c.label LEFT JOIN FETCH c.genre LEFT JOIN FETCH c.musicians ORDER BY c.id", CD.class);



          if (startPosition != null) {

                 findAllQuery.setFirstResult(startPosition);

          }

          if (maxResult != null) {

                 findAllQuery.setMaxResults(maxResult);

          }



          final List<CD> results = findAllQuery.getResultList();

          return results;

    }	
    
	
	public List<CD> findByGenre(Long genreId) {
		
		TypedQuery<CD> query = getEntityManager().createQuery("select c from CD c where c.genre.id = :genreId", CD.class);
		query.setParameter("genreId", genreId);
		List<CD> result = query.getResultList();
		return result;
		
	}
	
	public List<String> findAllImages() {
		
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();

		// Populate count

		CriteriaQuery<String> countCriteria = builder.createQuery(String.class);
		Root<CD> root = countCriteria.from(CD.class);
		countCriteria = countCriteria.select(root.<String>get("imageURL")).where(builder.isNotNull(root.<String>get("imageURL")));
		List<String> result = getEntityManager().createQuery(countCriteria).getResultList();
		return result;
	}
}