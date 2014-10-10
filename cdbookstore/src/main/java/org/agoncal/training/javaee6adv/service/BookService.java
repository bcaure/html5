package org.agoncal.training.javaee6adv.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.agoncal.training.javaee6adv.model.Book;
import org.agoncal.training.javaee6adv.model.Category;

@Stateless
@LocalBean
@Named
public class BookService extends AbstractService<Book> implements Serializable {

	private static final long serialVersionUID = -1L;

	public BookService() {
		super(Book.class);
	}

	@Override
	protected Predicate[] getSearchPredicates(Root<Book> root, Book example) {

		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String title = example.getTitle();
		if (title != null && !"".equals(title)) {
			predicatesList
					.add(builder.like(builder.lower(root.<String> get("title")), '%' + title.toLowerCase() + '%'));
		}
		String description = example.getDescription();
		if (description != null && !"".equals(description)) {
			predicatesList.add(builder.like(builder.lower(root.<String> get("description")),
					'%' + description.toLowerCase() + '%'));
		}
		String imageURL = example.getImageURL();
		if (imageURL != null && !"".equals(imageURL)) {
			predicatesList.add(builder.like(builder.lower(root.<String> get("imageURL")),
					'%' + imageURL.toLowerCase() + '%'));
		}
		String isbn = example.getIsbn();
		if (isbn != null && !"".equals(isbn)) {
			predicatesList.add(builder.like(builder.lower(root.<String> get("isbn")), '%' + isbn.toLowerCase() + '%'));
		}
		Integer nbOfPages = example.getNbOfPages();
		if (nbOfPages != null && nbOfPages.intValue() != 0) {
			predicatesList.add(builder.equal(root.get("nbOfPages"), nbOfPages));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);

	}
	
	public List<String> findAllImages() {
		
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();

		// Populate count

		CriteriaQuery<String> countCriteria = builder.createQuery(String.class);
		Root<Book> root = countCriteria.from(Book.class);
		countCriteria = countCriteria.select(root.<String>get("imageURL")).where(builder.isNotNull(root.<String>get("imageURL")));
		List<String> result = getEntityManager().createQuery(countCriteria).getResultList();
		return result;
	}
	
	public List<Book> findByCategory(Long categoryId) {
		
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();

		// Populate count

		CriteriaQuery<Book> countCriteria = builder.createQuery(Book.class);
		Root<Book> root = countCriteria.from(Book.class);
		countCriteria = countCriteria.select(root).where(builder.equal(root.<Category>get("category").<Long>get("id"),categoryId));
		List<Book> result = getEntityManager().createQuery(countCriteria).getResultList();
		return result;
		
	}

}