package org.agoncal.training.javaee6adv.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Customer implements Serializable
{

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   private Long id;
   @Version
   @Column(name = "version")
   private int version;

   @Column(name = "first_name")
   @Size(min = 2, max = 50)
   @NotNull
   private String firstname;

   @Column(name = "last_name")
   @Size(min = 2, max = 50)
   @NotNull
   private String lastname;

   @Column
   private String telephone;

   @Column
   private String email;

   @Column(name = "date_of_birth")
   @Temporal(TemporalType.DATE)
   @Past
   private Date dateOfBirth;

   @Transient
   private int age;

   
   @Embedded @Valid
   Address address = new Address();
   
   public Long getId()
   {
      return this.id;
   }

   public void setId(final Long id)
   {
      this.id = id;
   }

   public int getVersion()
   {
      return this.version;
   }

   public void setVersion(final int version)
   {
      this.version = version;
   }


public Address getAddress() {
	return address;
}

public void setAddress(Address address) {
	this.address = address;
}

@Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (!(obj instanceof Customer))
      {
         return false;
      }
      Customer other = (Customer) obj;
      if (id != null)
      {
         if (!id.equals(other.id))
         {
            return false;
         }
      }
      return true;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      return result;
   }

   public String getFirstname()
   {
      return firstname;
   }

   public void setFirstname(String firstname)
   {
      this.firstname = firstname;
   }

   public String getLastname()
   {
      return lastname;
   }

   public void setLastname(String lastname)
   {
      this.lastname = lastname;
   }

   public String getTelephone()
   {
      return telephone;
   }

   public void setTelephone(String telephone)
   {
      this.telephone = telephone;
   }

   public String getEmail()
   {
      return email;
   }

   public void setEmail(String email)
   {
      this.email = email;
   }

   public Date getDateOfBirth()
   {
      return dateOfBirth;
   }

   public void setDateOfBirth(Date dateOfBirth)
   {
      this.dateOfBirth = dateOfBirth;
   }

   public int getAge()
   {
      return age;
   }

   public void setAge(int age)
   {
      this.age = age;
   }

   public String getStreet1()
   {
      return address.getStreet1();
   }

   public void setStreet1(String street1)
   {
      this.address.setStreet1(street1);
   }

   public String getStreet2()
   {
      return address.getStreet2();
   }

   public void setStreet2(String street2)
   {
	   this.address.setStreet2(street2);
   }

   public String getCity()
   {
      return address.getCity();
   }

   public void setCity(String city)
   {
      this.address.setCity(city);
   }

   public String getState()
   {
      return address.getState();
   }

   public void setState(String state)
   {
      this.address.setState(state);
   }

   public String getZipcode()
   {
      return address.getZipcode();
   }

   public void setZipcode(String zipcode)
   {
      this.address.setZipcode(zipcode);
   }

   public String getCountry()
   {
      return address.getCountry();
   }

   public void setCountry(String country)
   {
      this.address.setCountry(country);
   }

   @Override
   public String toString()
   {
      String result = getClass().getSimpleName() + " ";
      if (firstname != null && !firstname.trim().isEmpty())
         result += "firstname: " + firstname;
      if (lastname != null && !lastname.trim().isEmpty())
         result += ", lastname: " + lastname;
      if (telephone != null && !telephone.trim().isEmpty())
         result += ", telephone: " + telephone;
      if (email != null && !email.trim().isEmpty())
         result += ", email: " + email;

      return result;
   }
}