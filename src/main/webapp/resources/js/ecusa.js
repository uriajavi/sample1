/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * OnClick handler for reset password button in login form
 * @returns {Boolean}
 */
function resetPasswdOnClick(){
        if (window.confirm("¿Está seguro de que desea reestablecer su contraseña?\n Se le enviará una nueva por email.")){
            document.getElementById('loginform:password').value='*';
            document.getElementById('loginform').setAttribute('novalidate','');
            return true;
        }else return false;
}
/**
 * OnSelect handler for criteria combo box in user's management page
 * @returns {undefined}
 */
function criteriaOnChange(){
    if(document.getElementById("criteria").value==="dni"){
        document.getElementById("DNIfields").style.display="table-cell";
        document.getElementById("DNI").value="";
        document.getElementById("DNI").required=true;
        document.getElementById("DNI").focus();
    }else{
        document.getElementById("DNI").value="";
        document.getElementById("DNIfields").style.display="none";
        document.getElementById("DNI").required=false;
    }
    
}
/**
 * Handler for ajax error events
 * @param {type} data 
 * @returns {undefined}
 */
function ajaxError(data){
    console.log("Error Description: "+data.description);
    console.log("Error Name: "+data.errorName);
    console.log("Error errorMessage: "+data.errorMessage);
    console.log("httpError: "+data.httpError);
    console.log("emptyResponse: "+data.emptyResponse);
    console.log("maleformed: "+data.maleformed);
    console.log("serverError: "+data.serverError);
}
/**
 * Select all checkbox change event
 */
function selectAll(){
    var cks=document.getElementsByClassName('user-list-ck');
    for(var i=0;i<cks.length;i++)
        cks[i].checked=document.getElementById('userstable:allusers').checked;
}
/**
 * OnClick handler for delete users button in users data page
 * @returns {Boolean}
 */
function deleteUsersOnClick(){
        if (window.confirm("¿Está seguro de que desea borrar los usuarios seleccionados?\n"+
                "ATENCIÓN: Esta operación no se podrá deshacer!!!")){
            document.getElementById('userstableform').setAttribute('novalidate','');
            return true;
        }else return false;
}