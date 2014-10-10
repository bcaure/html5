package org.agoncal.training.javaee6adv.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class PurchaseOrder implements Serializable
{

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   private Long id;
   @Version
   @Column(name = "version")
   private int version;

   @Column
   private int quantity;

   @Column(name = "order_date")
   @Temporal(TemporalType.DATE)
   private Date orderDate;

   @ManyToOne
   private Customer customer;

   @OneToMany
   private Set<OrderLine> orderLines = new HashSet<OrderLine>();

   @Embedded @Valid
   private CreditCard creditCard = new CreditCard();
   
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

   public CreditCard getCreditCard() {
	return creditCard;
}

public void setCreditCard(CreditCard creditCard) {
	this.creditCard = creditCard;
}

@Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (!(obj instanceof PurchaseOrder))
      {
         return false;
      }
      PurchaseOrder other = (PurchaseOrder) obj;
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

   public Address getAddress() {
	return address;
}

public void setAddress(Address address) {
	this.address = address;
}

public int getQuantity()
   {
      return quantity;
   }

   public void setQuantity(int quantity)
   {
      this.quantity = quantity;
   }

   public Date getOrderDate()
   {
      return orderDate;
   }

   public void setOrderDate(Date orderDate)
   {
      this.orderDate = orderDate;
   }

   public Customer getCustomer()
   {
      return this.customer;
   }

   public void setCustomer(final Customer customer)
   {
      this.customer = customer;
   }

   public Set<OrderLine> getOrderLines()
   {
      return this.orderLines;
   }

   public void setOrderLines(final Set<OrderLine> orderLines)
   {
      this.orderLines = orderLines;
   }

   public String getStreet1()
   {
      return address.street1;
   }

   public void setStreet1(String street1)
   {
      this.address.street1 = street1;
   }

   public String getStreet2()
   {
      return address.street2;
   }

   public void setStreet2(String street2)
   {
      this.address.street2 = street2;
   }

   public String getCity()
   {
      return address.city;
   }

   public void setCity(String city)
   {
      this.address.city = city;
   }

   public String getState()
   {
      return address.state;
   }

   public void setState(String state)
   {
      this.address.state = state;
   }

   public String getZipcode()
   {
      return address.zipcode;
   }

   public void setZipcode(String zipcode)
   {
      this.address.zipcode = zipcode;
   }

   public String getCountry()
   {
      return address.country;
   }

   public void setCountry(String country)
   {
      this.address.country = country;
   }

   public String getCreditCardNumber()
   {
      return creditCard.creditCardNumber;
   }

   public void setCreditCardNumber(String creditCardNumber)
   {
      this.creditCard.creditCardNumber = creditCardNumber;
   }

   public CreditCardType getCreditCardType()
   {
      return creditCard.creditCardType;
   }

   public void setCreditCardType(CreditCardType creditCardType)
   {
      this.creditCard.creditCardType = creditCardType;
   }

   public String getCreditCardExpDate()
   {
      return creditCard.creditCardExpDate;
   }

   public void setCreditCardExpDate(String creditCardExpDate)
   {
      this.creditCard.creditCardExpDate = creditCardExpDate;
   }

   @Override
   public String toString()
   {
      String result = getClass().getSimpleName() + " ";
      result += "quantity: " + quantity;
      if (address.street1 != null && !address.street1.trim().isEmpty())
         result += ", street1: " + address.street1;
      if (address.street2 != null && !address.street2.trim().isEmpty())
         result += ", street2: " + address.street2;
      if (address.city != null && !address.city.trim().isEmpty())
         result += ", city: " + address.city;
      if (address.state != null && !address.state.trim().isEmpty())
         result += ", state: " + address.state;
      if (address.zipcode != null && !address.zipcode.trim().isEmpty())
         result += ", zipcode: " + address.zipcode;
      if (address.country != null && !address.country.trim().isEmpty())
         result += ", country: " + address.country;
      if (creditCard.creditCardNumber != null && !creditCard.creditCardNumber.trim().isEmpty())
         result += ", creditCardNumber: " + creditCard.creditCardNumber;
      if (creditCard.creditCardExpDate != null && !creditCard.creditCardExpDate.trim().isEmpty())
         result += ", creditCardExpDate: " + creditCard.creditCardExpDate;
      return result;
   }
}