function drawArrowLeft(ctx, x, y, l, w, lineColour, fillingColour) {
    ctx.beginPath();
    ctx.moveTo(x, y);
    ctx.lineTo(x+l, y);
    ctx.lineTo(x+l, y-w/4);
    ctx.lineTo(x+l+l/5, y+w/2);
    ctx.lineTo(x+l, y+w+w/4);
    ctx.lineTo(x+l, y+w);
    ctx.lineTo(x, y+w);           
    ctx.closePath();
    ctx.strokeStyle = lineColour;
    ctx.stroke();
    ctx.fillStyle = fillingColour;
    ctx.fill();
}

function drawArrowUp(ctx, x, y, l, w, lineColour, fillingColour) {
    ctx.beginPath();
    ctx.moveTo(x, y);
    ctx.lineTo(x, y-l);
    ctx.lineTo(x-w/4, y-l);
    ctx.lineTo(x+w/2, y-l-l/5);
    ctx.lineTo(x+w+w/4, y-l);
    ctx.lineTo(x+w, y-l);
    ctx.lineTo(x+w, y);           
    ctx.closePath();
    ctx.strokeStyle = lineColour;
    ctx.stroke();
    ctx.fillStyle = fillingColour;
    ctx.fill();
}

function importUrl(direction) {
	var newIndex = indexPage;
	if (direction == 'prev') newIndex--;
	else newIndex++;
	if (importTemplate('#placeholder', '#import'+newIndex)) {
		indexPage = newIndex;
	}
}

function importTemplate(placeholder, importName) {
	var placeholder = document.querySelector(placeholder);
	var link = document.querySelector(importName);
	if (link != null) {
		var template = link.import.querySelector('template');
		var clone = document.importNode(template.content, true);
		placeholder.innerHTML = '';
		placeholder.appendChild(clone);
		return true;
	} else 
        return false;
}	
