// Copyright ScenPro, Inc 2007

// $Header: /cvsshare/content/cvsroot/cdecurate/WebRoot/js/CreatePV.js,v 1.2 2009-04-10 14:53:45 hebell Exp $
// $Name: not supported by cvs2svn $

 var searchWindow = null;

   //  checks the status message everytime page opens and alerts the  message
   function displayStatus(vStatusMessage)
   {
        if (vStatusMessage == null || vStatusMessage == "no message")
            vStatusMessage = "";
        if (vStatusMessage != "") 
        {
            alert(vStatusMessage);
        }
   }

  function hourglass()
  {
    document.body.style.cursor = "wait";
  }

  function down_hourglass()
  {
    document.body.style.cursor = "default";
  }

  function AccessEVS()
  {
    if (searchWindow && !searchWindow.closed)
       searchWindow.focus();
    else
    {
       searchWindow = window.open("jsp/OpenSearchWindowDef.jsp", "searchWindow", "width=950,height=450,resizable=yes,scrollbars=yes");
       document.SearchActionForm.searchEVS.value = "PermissableValue";
    }
 }


 
  function clearBoxes()
  {    
   // document.createPVForm.txtPermValue.value = "";
   // document.createPVForm.CreateDescription.value = "";
//document.createPVForm.selShortMeanings.value="";    
    //document.createPVForm.EndDate.value="";    
    //document.createPVForm.BeginDate.value="";  
    //currentDate();  
    document.createPVForm.pageAction.value = "clearBoxes";
    document.createPVForm.submit();
  }

  function SubmitValidate(origin)
  {
        //store the selected valid value name in the hidden field
      	if(document.createPVForm.selValidValue != null)
      	{
	        var iInd = document.createPVForm.selValidValue.selectedIndex;
	        if (iInd > -1)
	        {
	          var vvtext = document.createPVForm.selValidValue[iInd].text;
	          document.createPVForm.txValidValue.value = vvtext;
	        }
      	}
        hourglass();
        if (origin == "validate")
      	{
        	document.createPVForm.pageAction.value = "validate";
        	window.status = "Validating data, it may take a minute, please wait.....";
      	}
      	else if (origin == "submit")
        {
            document.createPVForm.pageAction.value = "submit";
            window.status = "Submitting data, it may take a minute, please wait.....";
	     } 
        document.createPVForm.Message.style.visibility="visible";
	    //disable the buttons
	    document.createPVForm.btnValidate.disabled = true;
	    document.createPVForm.btnClear.disabled = true;
	   	document.createPVForm.btnBack.disabled = true;
	    //submit the form
        document.createPVForm.submit();
  }
 
 function isDateValid()
 {
    var beginDate = document.createPVForm.BeginDate.value;
    var endDate = document.createPVForm.EndDate.value;
    var acAction = document.createPVForm.PVAction.value;
    if (acAction == "editPV" || acAction == "EditPV") acAction = "Edit";
    return areDatesValid(beginDate, endDate, acAction);
 }


   function SubmitPV()
  {
     hourglass();
     document.createPVForm.pageAction.value = "submit";
     document.createPVForm.Message.style.visibility="visible";
     window.status = "Submitting data, it may take a minute, please wait.....";
     document.createPVForm.submit();
  }

  function createNewValue()
  {
      document.createPVForm.pageAction.value  = "createNewVM";
 	    document.createPVForm.submit();

  /*  comment this to put the hyperlink
    var list1 = document.createPVForm.selShortMeanings;
    var list2 = document.createPVForm.txtPermValue;
    var currentValue = document.createPVForm.txtPermValue.value;
    var selectedIdx = list1.selectedIndex;
    if (selectedIdx >= 0)
       var selectedValue = list1.options[selectedIdx].text;
    if (selectedValue == "* Create New *")
    {  
      document.FormVM.hiddenPValue.value = currentValue;
	    document.FormVM.submit();
    }
    else
    {
      if (selectedValue != document.createPVForm.selShortMeanings.options[selectedIdx].value)
        document.createPVForm.selShortMeanings.options[selectedIdx].value = selectedValue;
      var DescText = document.createPVForm.selMeaningDesc.options[selectedIdx].value;
      document.createPVForm.CreateDescription.value = DescText;
    }  */
  }

  function Back()
  {
    hourglass();
    document.createPVForm.pageAction.value  = "backToVD";
    document.createPVForm.submit();
  }

  function searchVM()
  {
  //alert("in search vm");
    if (document.SearchActionForm.searchComp != null)
    {
        document.SearchActionForm.searchComp.value = "ValueMeaning";
        document.SearchActionForm.isValidSearch.value = "false";
    }
    if (searchWindow && !searchWindow.closed)
      searchWindow.close()
    searchWindow = window.open("jsp/OpenSearchWindow.jsp", "searchWindow", "width=875,height=570,top=0,left=0,resizable=yes,scrollbars=yes")
  }
  
