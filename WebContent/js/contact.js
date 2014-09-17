var websocket = null;
var model = {
  "contacts": [
    {"id":"1", "firstName":"Bernadette", "lastName":"Durand", "homeNumber":"04.56.47.45.65", "cellNumber":"06.12.33.45.99", "email":"bdurand@outlook.com"},
    {"id":"2", "firstName":"Ginette", "lastName":"Dumont", "homeNumber":"04.12.36.11.11", "cellNumber":"06.43.90.78.10", "email":"gdumont@gmail.com"},
    {"id":"3", "firstName":"Alain", "lastName":"Dupontel", "homeNumber":"05.12.34.11.88", "cellNumber":"06.78.44.68.22", "email":"alain.dupontel@gmail.com"},
    {"id":"4", "firstName":"Paul", "lastName":"Pole", "homeNumber":"05.11.34.11.88", "cellNumber":"06.78.44.68.22", "email":"pile.poele@gmail.com"},
    {"id":"5", "firstName":"Alain", "lastName":"Dupontel", "homeNumber":"05.12.34.11.88", "cellNumber":"06.78.44.68.22", "email":"toutoune@gmail.com"},
    {"id":"6", "firstName":"Josie", "lastName":"Gonzales", "homeNumber":"05.12.34.11.88", "cellNumber":"06.78.44.68.22", "email":"totopprout@gmail.com"},
    {"id":"7", "firstName":"Alain", "lastName":"Dumont", "homeNumber":"05.12.34.11.88", "cellNumber":"06.78.44.68.22", "email":"alain.dumont@aol.com"},
    {"id":"8", "firstName":"Barnab\u00E9", "lastName":"De la Vega", "homeNumber":"05.12.34.11.88", "cellNumber":"06.78.44.68.22", "email":"kikoo@lol.com"}
  ]
};

/**
 * View
 */
function showContact(contactDiv) {
	form = document.getElementById('form');
	form.style.display='block';
	if (contactDiv != null) {
		contactToDisplay = null;
		model.contacts.forEach(function(contact) {
			if (contact.id == contactDiv.functionalid) {
				contactToDisplay = contact;
				return;
			}
		});
		fillContactDiv(contactToDisplay, form);
	}
}

function add(button) {
	  contact = {};
	  contactInputs = document.getElementsByName('contactForm');
	  for (i = 0 ; i < contactInputs.length ; i++) {
	    if (button.innerText == 'OK') {
	      contact[contactInputs[i].id] = contactInputs[i].value;
	   }
	   contactInputs[i].value = '';
	  };
	  contact.id = contactInputs.functionalid;
	  if (button.innerText == 'OK') {
	    websocket = new WebSocket('ws://localhost:8080/websocket-backend/websocket');
	    websocket.onopen = function(event) {
	    	contactstring = JSON.stringify(contact);
	    	websocket.send(contactstring);
	    	console.log("Send message : "+contactstring);
	    }	    
	    websocket.onmessage = function(event) {
	    	console.log("Receive message : "+event.data);
	    	model.contacts.push(JSON.parse(event.data));
	    };   	

	  }
	  document.getElementById('form').style.display='none';
}

/** 
 * Controller 
 */
function init() {
  model.contacts.forEach(function(contact) {
	  contactDiv = document.getElementById('contactTemplate').cloneNode(true);
	  contactDiv.id = 'contact_'+contact.id;
	  document.getElementById('mainContentDiv').appendChild(fillContactDiv(contact, contactDiv));
  });
}
/** Observe model */
Array.observe(model.contacts, function(changes) {

    changes.forEach(function(change) {
    
      if (change.addedCount > 0) {
    	contactDiv = document.getElementById('contactTemplate').cloneNode(true);
  	    contactDiv.id = 'contact_'+contact.id;
        fillContactDiv(change.object[change.index], contactDiv);
        mainDiv = document.getElementById('mainContentDiv');
        mainDiv.insertBefore(contactDiv, mainDiv.childNodes[0]);
      }
    });
});

function fillContactDiv(contact, contactDiv) {
  contactDiv.functionalid = contact.id;  
  fillComponent(contactDiv.getElementsByClassName('firstName')[0], contact.firstName);  
  fillComponent(contactDiv.getElementsByClassName('lastName')[0], contact.lastName);
  fillComponent(contactDiv.getElementsByClassName('homeNumber')[0], contact.homeNumber);  
  fillComponent(contactDiv.getElementsByClassName('cellNumber')[0], contact.cellNumber);  
  fillComponent(contactDiv.getElementsByClassName('email')[0], contact.email);  
  return contactDiv;
}

function fillComponent(component, data) {
	if (component.textContent != undefined) component.textContent = data;
	if (component.value != undefined) component.value = data;	
}




