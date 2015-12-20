var websocket = null;
var url = 'ws://localhost:8080/websocket';
var model = {
  "contacts": []
};

function Contact(jsonObj) {
	if (jsonObj != undefined) {
		for (key in jsonObj) {
			this[key] = jsonObj[key];
		}
	}
	this.buildId = function () {
		return 'contact_'+this.id.firstName+'#'+this.id.lastName;
	}
	
	return this;
}

/** View */
function fillContactDiv(contact, contactDiv) {
  fillComponent(contactDiv.getElementsByClassName('firstName')[0], contact.id.firstName);  
  fillComponent(contactDiv.getElementsByClassName('lastName')[0], contact.id.lastName);
  fillComponent(contactDiv.getElementsByClassName('homeNumber')[0], contact.homeNumber);  
  fillComponent(contactDiv.getElementsByClassName('cellNumber')[0], contact.cellNumber);  
  fillComponent(contactDiv.getElementsByClassName('email')[0], contact.email);  
  return contactDiv;
}

function fillComponent(component, data) {
  if (component.textContent != undefined) component.textContent = data;
  if (component.value != undefined) component.value = data; 
}

/** CRUD events observer */
Array.observe(model.contacts, function(changes) {

  changes.forEach(function(change) {
  
    if (change.addedCount > 0) {
      
      // Add object
      var contact = change.object[change.index];
      var templateDiv = document.querySelector('template').content.getElementById('templateDiv')
      var contactDiv = templateDiv.cloneNode(true);
      contactDiv.id = contact.buildId();
	  contactDiv.querySelector('.removeLink').addEventListener("click", function() { deleteObject(contactDiv.id); });
      fillContactDiv(contact, contactDiv);
      var mainDiv = document.getElementById('mainContentDiv');
      mainDiv.insertBefore(contactDiv, mainDiv.childNodes[0]);
    
    } else if (change.removed && change.removed.length > 0) {
      
      var contact = change.removed[0];
      var contactDiv = document.getElementById(contact.buildId());
      contactDiv.parentNode.removeChild(contactDiv);
		
    } else if (change.type == 'update') {
      
      // Modify object
      var contact = change.object[change.name];
      var contactDiv = document.getElementById(contact.buildId());
      fillContactDiv(contact, contactDiv);
      
    }
  });
});

/** Load objects from DB */
function init() {
  
  var websocket = new WebSocket(url+'/load-contact');
  websocket.onopen = function(event) {
    websocket.send("{}");
  }     
  websocket.onmessage = function(event) {
    console.log("Receive contacts:"+event.data);
    var result = JSON.parse(event.data);
    result.forEach(function(jsonObj) {
      model.contacts.push(new Contact(jsonObj));
    });
  };
}


/** Populate form */
function showContact(e, contactDiv) {
  
  	// Populate and display form
	var form = document.querySelector('#panelForm');
	form.style.display='block';
	form.style.position = 'absolute';
	if (contactDiv != null) {
		var contactToDisplay = null;
		model.contacts.forEach(function(contact) {
			if (contact.buildId() == contactDiv.id) {
				contactToDisplay = contact;
				return;
			}
		});
		fillContactDiv(contactToDisplay, form);
	}
}

/** Form actions */
function ok() {
  
    // Fill model from inputs if "OK" was clicked
	var contact;
    contact = createObjectFromForm('contactForm');
    contact.id = createObjectFromForm('contactFormId');
	  
    // Send contact
    var websocket = new WebSocket(url+'/push-contact');
    websocket.onopen = function(event) {
    	var contactstring = JSON.stringify(contact);
    	websocket.send(contactstring);
    	console.log("Send contact : "+contactstring);
    }	    
    // Receive response
    websocket.onmessage = function(event) {
    	console.log("Receive contact : "+event.data);
		var contact = new Contact(JSON.parse(event.data));
    	for (i = 0; i < model.contacts.length;i++) { // check for modification of existing contact
    	  if (model.contacts[i].buildId() == contact.buildId()) {
    	    model.contacts[i] = contact; 
    	    contact = null; 
    	    break;
    	  }
    	}
    	if (contact != null) { // this is new contact
    	  model.contacts.push(contact);
    	}
    };   	
    exitForm();
}

function exitForm() {
  // Clear inputs
  document.querySelector('form').reset();
  document.querySelector('#panelForm').style.display='none';
}

function createObjectFromForm(inputsName) {
  var result = new Contact();
  var inputs = document.querySelectorAll('input[name="'+inputsName+'"]');
  for (var i = 0; i < inputs.length; ++i) {
    var input = inputs[i];
    result[input.id] = input.value;
  }
  return result;
}

/** Delete contact */
function deleteObject(divId) {
  var i = 0;	
  for (; i < model.contacts.length; i++) {
	console.log(model.contacts[i].id);
	if (model.contacts[i].buildId() == divId) {
		break;
	}
  }
  model.contacts.splice(i, 1);	
}






