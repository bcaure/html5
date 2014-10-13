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

/** Controller */
// Bind controller
Array.observe(model.contacts, function(changes) {

  changes.forEach(function(change) {
  
    if (change.addedCount > 0) {
      contact = change.object[change.index];
      contactDiv = document.getElementById('contactTemplate').cloneNode(true);
      contactDiv.id = buildId(contact);
      fillContactDiv(contact, contactDiv);
      mainDiv = document.getElementById('mainContentDiv');
      mainDiv.insertBefore(contactDiv, mainDiv.childNodes[0]);
    } else if (change.removed && change.removed.length > 0) {
       // TODO implement delete
    } else if (change.type == 'update') { 
      contact = change.object[change.name];
      contactDiv = document.getElementById(buildId(contact));
      fillContactDiv(contact, contactDiv);
    }
  });
});

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


/**
 * Model
 */
function showContact(contactDiv) {
	form = document.getElementById('form');
	form.style.display='block';
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

function add(button) {
  
    // Clear inputs and fill contact model from inputs if "OK" was clicked
	  contact = {};
	  contactInputs = document.getElementsByName('contactForm');
	  for (i = 0 ; i < contactInputs.length ; i++) {
	    if (button.innerText == 'OK') {
	      contact[contactInputs[i].id] = contactInputs[i].value;
	    }
	   contactInputs[i].value = '';
	  };
	  if (button.innerText == 'OK') {
	    websocket = new WebSocket(url+'/push-contact');
	    websocket.onopen = function(event) {
	    	contactstring = JSON.stringify(contact);
	    	websocket.send(contactstring);
	    	console.log("Send contact : "+contactstring);
	    }	    
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

	  }
	  document.getElementById('form').style.display='none';
}

function importContact(input) {
  var websocketImport = new WebSocket(url+'/import-contact');
  websocketImport.onopen = function (ev) {
    
    // Read each file 
    for (var i = 0, file; file = input.files[i]; ++i) {
      var reader = new FileReader();
      reader.onload = function(e) {
        var lines = this.result.split('\n');   
        for (var index = 0;index < lines.length; index++) {
          // Send data
          console.log("Send line of csv file ");  
          websocketImport.send(lines[index]);        
        }
      };
      reader.onerror = function(e) {
        console.log(e);
      };
      reader.readAsText(file);
    }
    
    // Process result
    websocketImport.onmessage = function(event) {
      console.log("Receive csv line result: "+event.data);
      result = JSON.parse(event.data);
      result.forEach(function(contact) {
        model.contacts.push(contact);
      });
    };
  };   
}







