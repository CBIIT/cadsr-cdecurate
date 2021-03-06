var rX;
var rY;
var menuVertPx = 20;
var menuHorizPx = 20;
var menuVisibleElement = null;
var menuHoldElement = null;
var menuSrcElement = null;
var menuSrcElementHold = null;

var menuBackgroundColor = "#d8d8df";
var menuRootBackgroundColor = "#5c5c5c";
var menuTextColor = "#000000";
var menuFocusColor = "#ffffff";
var menuDisabledColor = "#777777";
var menuRootFocusColor = "#6B78B4";

var prevRowObj = null;
var prevRowBGColor;

function loaded(menus){
    var menu = document.getElementById(menus);
    menuCheckEnabled(menu);
}
function menuHide() {
	if (menuVisibleElement !== null) {
		menuVisibleElement.style.display = "none";
	}
	menuVisibleElement = menuHoldElement;
	menuSrcElement = menuSrcElementHold;
	menuHoldElement = null;
	menuSrcElementHold = null;
}
function setBorder(obj, j){
	var exit = false;
	var parentObj = obj.parentNode;
	rowColor = parentObj.parentNode.style.backgroundColor;
	if (rowColor == "rgb(255, 232, 124)") {
		obj.style.border = '1px solid #FFE87C' ;
		exit = true;
	}
	else if (rowColor == "#ffe87c"){
	     obj.style.border = '1px solid #FFE87C' ;
	     exit = true;
	}    
    if(!exit) {
	     if ((j%2) === 0){
	       obj.style.border = '1px solid #dfdfdf' ;
	     }else{
	       obj.style.border = '1px solid #FFFFFF' ;
	     } 
	  }   
}

function setHighlightRow(obj){
     if (obj.nodeName == "TR"){
        if (prevRowObj != null){
            prevRowObj.style.backgroundColor = prevRowBGColor;
            if (prevRowObj.childNodes[3].firstChild.nextSibling != null){
                var object = prevRowObj.childNodes[3].firstChild.nextSibling;
             }else{
                var object = prevRowObj.childNodes[1].firstChild;
             }   
            object.style.borderColor = prevRowBGColor;
         }
        prevRowBGColor = obj.style.backgroundColor;
        prevRowObj = obj;
        prevRowObj.style.backgroundColor = "#FFE87C";
        return;
    }
    setHighlightRow(obj.parentNode);
}

function menuShow(obj, evnt, highlight) {
    if(highlight == "yes"){
      setHighlightRow(obj);
    }  
    var menuID = obj.getAttribute("menuID");
	var rowId = obj.getAttribute("rowId");
	document.searchResultsForm.selectedRowId.value = rowId;
	if (menuID == null) {
		return;
	}
	var srcElementHold = null;
	if (evnt.srcElement == undefined) {
		if (menuSrcElement == evnt.currentTarget) {
			menuHide();
			return;
		}
		srcElementHold = evnt.currentTarget;
		
	} else {
		if (menuSrcElement == evnt.srcElement) {
			menuHide();
			return;
		}
		srcElementHold = evnt.srcElement;
	}
	var menu = document.getElementById(menuID);
	menuHide();
	menuHoldElement = menu;
	menuSrcElementHold = srcElementHold;
	menu.style.display = "block";
	var offsetX;
	var offsetY;
	var menuType = menu.getAttribute("menuType");
	if (menuType !== null && menuType == "float") {
		if (srcElementHold.width > 0) {
			offsetX = srcElementHold.width;
			offsetY = srcElementHold.height;
		} else {
			offsetX = menuHorizPx;
			offsetY = menuVertPx;
		}
	} else {
		offsetX = 0;
		offsetY = menuVertPx;
	}
	var top = 0;
	var left = 0;
	if (evnt.offsetX == undefined || evnt.offsetY == undefined) {
		menuObjPos(srcElementHold);
	    	top = rY;
		left = rX;
		menuObjPos(menu.parentNode);
		top = top - rY + offsetY;
		left = left - rX + offsetX;
	} else {
		var scrollTop = document.documentElement.scrollTop > document.body.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop; 
		
		var scrollLeft = document.documentElement.scrollLeft > document.body.scrollLeft ? document.documentElement.scrollLeft : document.body.scrollLeft; 
		
		top = scrollTop + evnt.clientY - evnt.offsetY + offsetY;
		left = scrollLeft + evnt.clientX - evnt.offsetX + offsetX;
		
	}
	
	var windowHeight = f_clientHeight();
	var heightOfMenu = menu.clientHeight;
	var topOfWindow = document.documentElement.scrollTop > document.body.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop; 
	var bottomOfMenu = top + heightOfMenu;
	var bottomOfWindow = topOfWindow + windowHeight;
	
	var difference = bottomOfMenu - bottomOfWindow;
	
	if (difference > 0 && menuType == "float") {
		if (top - difference < 0) {
			top = 0;
		} else {
			if (top - difference < topOfWindow) {
				top = topOfWindow;
			} else {
				top = top - difference;
			}
		}
	}
	menu.style.top = top + "px";
	menu.style.left = left + "px";
}

function menuItemFocus(mitem) {
	var miEnable = mitem.getAttribute("menuEnable");
	if (miEnable == null || miEnable != "false") {
		mitem.style.backgroundColor = menuFocusColor;
	} else {
	}
	mitem.style.cursor = "default";
}
function menuItemNormal(mitem) {
	mitem.style.backgroundColor = menuBackgroundColor;
	var miEnable = mitem.getAttribute("menuEnable");
	if (miEnable == null || miEnable != "false") {
		//mitem.style.color = menuTextColor;
	}
}
function menuRootOver(obj, evnt) {
	obj.style.backgroundColor = menuRootFocusColor;
	if (menuVisibleElement !== null) {
		var menuID = obj.getAttribute("menuID");
	    if (menuID !== null) {
			if (evnt.srcElement === undefined) {
				menuSrcElement = evnt.currentTarget;
			} else {
				menuSrcElement = evnt.srcElement;
			}
			var menu = document.getElementById(menuID);
			menuVisibleElement.style.display = "none";
			menuVisibleElement = menu;
			menuVisibleElement.style.display = "block";
			var offsetX;
			var offsetY;
			var menuType = menu.getAttribute("menuType");
			if (menuType !== null && menuType == "float") {
				if (menuSrcElement.width > 0) {
					offsetX = menuSrcElement.width;
					offsetY = menuSrcElement.height;
				} else {
					offsetX = menuHorizPx;
					offsetY = menuVertPx;
				}
			} else {
				offsetX = 0;
				offsetY = menuVertPx;
			}
			var top = 0;
			var left = 0;
			if (evnt.offsetX == undefined || evnt.offsetY == undefined) {
				menuObjPos(menuSrcElement);
				top = rY;
				left = rX;
				menuObjPos(menu.parentNode);
				top = top - rY + offsetY;
				left = left - rX + offsetX;
			} else {
				top = document.body.parentElement.scrollTop + evnt.clientY - evnt.offsetY + offsetY;
				left = document.body.parentElement.scrollLeft + evnt.clientX - evnt.offsetX + offsetX;
			}

			menu.style.top = top + "px";
			menu.style.left = left + "px";
		}
	}
}
function menuRootOut(obj, evnt) {
	obj.style.backgroundColor = menuRootBackgroundColor;
}
function menuOver(obj, evnt) {
	obj.style.backgroundColor = menuRootFocusColor;
}	
function menuCheckEnabled(menu) {
	var len;
	if (menu.children == undefined) {
		len = menu.childNodes.length;
	} else {
		len = menu.children.length;
	}
	var cnt;
	for (cnt = 0; cnt < len; ++cnt) {
		var child;
		if (menu.children == undefined) {
			child = menu.childNodes[cnt];
		} else {
			child = menu.children[cnt];
		}
		menuCheckEnabled(child);
	}
	if (menu.getAttribute !== undefined) {
		var menuFlag = menu.getAttribute("menuEnable");
		if (menuFlag !== null) {
			if (menuFlag == "false" || menuFlag == false) {
				menu.style.color = menuDisabledColor;
			} else {
				if (menuFlag != "true" && menuFlag !== true) {
					alert("The 'menuFlag' attribute is invalid on '" + menu.innerHTML + "', valid values are 'true' and 'false', case sensitive.");
				}
			}
		}
	}
}
function menuObjPos(obj) {
	var tagBody = document.getElementsByTagName("body")[0];
	var oX = obj.offsetLeft;
	var oY = obj.offsetTop;
	
	// finds the absolute position of the object
	while (obj.parentNode) {
		if (obj.parentNode.tagName != "TR") { 
            // Table Cells are relative to the Table not the Row. Adding the
            // Row offset causes a position miscalculation.
			var nX = obj.parentNode.offsetLeft;
			var nY = obj.parentNode.offsetTop;
			oX = oX + nX;
			oY = oY + nY;
		}
		if (obj == tagBody) {
			break;
		} else {
			obj = obj.parentNode;
		}
	}
	rX = oX;
	rY = oY;
}
 //submits the form to display 'Create New DE' window
 function callDENew(user){
   if (checkUser(user)) {    
     document.newDEForm.submit();
  }   
 }
 //submits the form to display 'Create New DEC' window
 function callDECNew(user){
  if (checkUser(user)) {
     document.newDECForm.submit();
  }
 }
//submits the form to display 'create New VD window'
 function callVDNew(user){
  if (checkUser(user)) {
     document.newVDForm.submit();
  }   
 }  
 
 //submits the form to display 'create New Concept Class window'
 function callCCNew(user){
  if (checkUser(user)){
     document.newCCForm.submit();
  }
 } 
//submits the form to display 'Home Page'
 function showHomePage(){
    document.homePageForm.submit();
 } 
 function displayStepsToFollow(user){
   if (checkUser(user)){
     alert("Please follow the steps below.\n 1. Perform a Search.\n 2. Click on the action icon for desired result.\n 3. Select 'New Using Existing' in the pop up action menu.");
   } 
 }
 function displayStepsToFollow2(user){
   if (checkUser(user)){
     alert("Please follow the steps below.\n 1. Perform a Search. \n 2. Click on the action icon for desired result. \n 3. Select 'New Version' in the pop up action menu.");
   }  
 }  

 //functions for determining window height and width.
 function f_clientWidth() {
	return f_filterResults (
		window.innerWidth ? window.innerWidth : 0,
		document.documentElement ? document.documentElement.clientWidth : 0,
		document.body ? document.body.clientWidth : 0
	);
}
function f_clientHeight() {
	return f_filterResults (
		window.innerHeight ? window.innerHeight : 0,
		document.documentElement ? document.documentElement.clientHeight : 0,
		document.body ? document.body.clientHeight : 0
	);
}
function f_scrollLeft() {
	return f_filterResults (
		window.pageXOffset ? window.pageXOffset : 0,
		document.documentElement ? document.documentElement.scrollLeft : 0,
		document.body ? document.body.scrollLeft : 0
	);
}
function f_scrollTop() {
	return f_filterResults (
		window.pageYOffset ? window.pageYOffset : 0,
		document.documentElement ? document.documentElement.scrollTop : 0,
		document.body ? document.body.scrollTop : 0
	);
}
function f_filterResults(n_win, n_docel, n_body) {
	var n_result = n_win ? n_win : 0;
	if (n_docel && (!n_result || (n_result > n_docel)))
		n_result = n_docel;
	return n_body && (!n_result || (n_result > n_body)) ? n_body : n_result;
}