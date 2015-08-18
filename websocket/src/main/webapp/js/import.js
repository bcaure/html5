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







