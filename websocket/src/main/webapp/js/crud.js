var websocket = null;
var url = 'ws://localhost:8080/websocket';
var model = {
  "contacts": []
};

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

function buildId(contact) {
  return 'contact_'+contact.id.firstName+'#'+contact.id.lastName;
}

/** CRUD events observer */
Array.observe(model.contacts, function(changes) {

  changes.forEach(function(change) {
  
    if (change.addedCount > 0) {
      
      // Add object
      contact = change.object[change.index];
      templateDiv = document.querySelector('template').content.getElementById('templateDiv')
      contactDiv = templateDiv.cloneNode(true);
      contactDiv.id = buildId(contact);
      fillContactDiv(contact, contactDiv);
      mainDiv = document.getElementById('mainContentDiv');
      mainDiv.insertBefore(contactDiv, mainDiv.childNodes[0]);
    
    } else if (change.removed && change.removed.length > 0) {
      
       // TODO implement Delete object
      
    } else if (change.type == 'update') {
      
      // Modify object
      contact = change.object[change.name];
      contactDiv = document.getElementById(buildId(contact));
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
    result = JSON.parse(event.data);
    result.forEach(function(contact) {
      model.contacts.push(contact);
    });
  };
}


/** Populate form */
function showContact(e, contactDiv) {
  
  // Mouse position
  var posx = 0;
  var posy = 0;
  if (!e) var e = window.event;
  if (e.pageX || e.pageY){
   posx = e.pageX;
   posy = e.pageY;
  }
  
  // Populate and display form
	form = document.querySelector('form');
	form.style.display='block';
	form.style.left = (posx-350)+'px';
	form.style.top = (posy-100)+'px';
	form.style.position = 'absolute';
	if (contactDiv != null) {
		contactToDisplay = null;
		model.contacts.forEach(function(contact) {
			if (buildId(contact) == contactDiv.id) {
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
    websocket = new WebSocket(url+'/push-contact');
    websocket.onopen = function(event) {
    	contactstring = JSON.stringify(contact);
    	websocket.send(contactstring);
    	console.log("Send contact : "+contactstring);
    }	    
    // Receive response
    websocket.onmessage = function(event) {
    	console.log("Receive contact : "+event.data);
    	contact = JSON.parse(event.data);
    	for (i = 0; i < model.contacts.length;i++) { // check for modification of existing contact
    	  if (model.contacts[i].id.firstName == contact.id.firstName && model.contacts[i].id.lastName == contact.id.lastName) {
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
  document.querySelector('form').style.display='none';
}

function createObjectFromForm(inputsName) {
  result = {};
  var inputs = document.querySelectorAll('input[name="'+inputsName+'"]');
  for (var i = 0; i < inputs.length; ++i) {
    var input = inputs[i];
    result[input.id] = input.value;
  }
  return result;
}

/** Delete contact */
function deleteObject() {
  
}






