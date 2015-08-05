/**
 * Proyecto:    Pegaso
 * Nombre:      ListPager
 * Autor:       Javier Martín Uría
 * Fecha:       21-nov-2012
 * Descripción: Clase para paginador que gestiona los datos que se muestran
 *              en las vistas de las listas de datos por páginas. Un objeto de 
 *              esta clase se guardará en la sesión almacenando toda la información
 *              sobre la consulta que realiza el usuario sobre una tabla concreta.
 *
 */

package useraccess.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import javax.enterprise.context.Dependent;

/**
 * Clase para paginador que gestiona los datos que se muestran
 * en las vistas de las listas de datos por páginas. Un objeto de 
 * esta clase se guardará en la sesión almacenando toda la información
 * sobre la consulta que realiza el usuario sobre una tabla concreta.
 * Para usar este paginador se deberá ralizar la siguiente secuencia de 
 * operaciones:<br>
 *          1º- Establecer el criterio de consulta con setCodCriterio().<br>
 *          2º- Establecer el valor del criterio de consulta con setValorCriterio().<br>
 *          3º- Obtener los datos para el paginador de la fuente de datos correspondiente.<br>
 *          4º- Pasar los datos anteriores al paginador mediante el método actualizar().<br>
 *              Este método se encarga además de actualizar el número de páginas y 
 *              la página actual.<br>
 *          5º- Usar los métodos de utilidad para gestionar el paginador.<br>
 * @author Javier Martín Uría.
 */
@Dependent
public class ListPager implements Serializable{

    //código del criterio de consulta
    private String      codCriterio;
    //valor del criterio de consulta
    private String      valorCriterio;
    //nº de filas por página. Será fijo y leido desde un parámetro de configuración
    private Short       datosPorPagina;
    //nº de páginas
    private Integer     numPaginas;
    //índice de página actual
    private Integer     pagActual;
    //datos de la consulta actual
    private Collection  datos;
    /** 
    * Crea un paginador nuevo con valores por defecto.
    * 
    */
    public ListPager(){
        
        this.codCriterio="";
        this.valorCriterio="";
        this.datosPorPagina=0;
        this.numPaginas=0;
        this.pagActual=0;
        
    }    
   /** 
    * Crea un paginador nuevo a partir de los parámetros.
    * @param codCriterio   Código del criterio de consulta.
    * @param valorCriterio Valor del criterio de consulta.
    * @param datosPorPagina Nº de filas de datos por página.
    * 
    */
    public ListPager(String       codCriterio,
                     String      valorCriterio,
                     Short       datosPorPagina){
        
        this.codCriterio=codCriterio;
        this.valorCriterio=valorCriterio;
        this.datosPorPagina=datosPorPagina;
        
    }
    //GETTERS
    /**
     * Obtiene el código del criterio de consulta.
     * @return código del criterio de consulta.
     */
    public  String  getCodCriterio(){
        return this.codCriterio;
    }
    /**
     * Obtiene el valor del criterio de consulta.
     * @return valor del criterio de consulta.
     */
    public String getValorCriterio(){
        return this.valorCriterio;
    }
    /**
     * Obtiene el número de datos por página que se mostrarán.
     * @return valor del número de datos por página.
     */
    public Short getDatosPorPagina(){
        return this.datosPorPagina;
    }
    /**
     * Obtiene el número de páginas de datos para la consulta actual del 
     * paginador.
     * @return valor del número de páginas.
     */
    public Integer getNumPaginas(){
        return this.numPaginas;
    }
    /**
     * Obtiene el número de la página actual de datos que proporciona el
     * paginador.
     * @return valor del número de página actual.
     */
    public Integer getPagActual(){
        return this.pagActual;
    }
    /**
     * Obtiene una colección con todos los datos que tiene el paginador 
     * asociados a la consulta actual.
     * @return la colección de datos.
     */
    public Collection  getDatos(){
        return this.datos;
    }
    //SETTERS
    /**
     * Establece el código del criterio de consulta para la consulta actual del
     * paginador.
     * @param codCriterio código del criterio de consulta.
     */
    public void setCodCriterio(String codCriterio){
        this.codCriterio=codCriterio;
    }
    /**
     * Establece el valor del criterio de consulta.
     * @param valorCriterio valor del criterio de consulta.
     */
    public void setValorCriterio(String valorCriterio){
        this.valorCriterio=valorCriterio;
    }
    /**
     * Establece el número de datos por página que se mostrarán.
     * @param datosPorPagina valor del número de datos por página.
     */
    public void setDatosPorPagina(Short datosPorPagina){
        this.datosPorPagina=datosPorPagina;
    }
    /**
     * Establece el número de páginas de datos para la consulta actual del 
     * paginador.
     * @param numPaginas valor del número de páginas.
     */
    private void setNumPaginas(Integer numPaginas){
         this.numPaginas=numPaginas;
    }
    /**
     * Establece el número de la página actual de datos que proporciona el
     * paginador.
     * @param pagActual valor del número de página actual.
     */
    public void setPagActual(Integer pagActual){
         this.pagActual=pagActual;
    }
    /**
     * Establece la colección de datos asociados a la consulta actual.
     * @param datos la colección de datos.
     */
    public void setDatos(Collection datos){
         this.datos=datos;
    }
    //Métodos de utilidad para manejo del paginador
    
   /**
    * Indica si la página actual es la primera página de datos.
    * @return true si es la primera. false si no lo es.
    */
    public Boolean isFirstPage(){
        if(this.numPaginas.intValue()!=0&&this.pagActual==1)
            return true;
        else 
            return false;
    }
   /**
    * Indica si la página actual es la última página de datos.
    * @return true si es la última página. false si no lo es.
    */
    public Boolean isLastPage(){
        if(this.numPaginas.intValue()!=0&&this.pagActual.equals(this.numPaginas))
            return true;
        else 
            return false;
    }

    /**
     * Obtiene la página de datos actual. 
     * @return Una colección con la página de datos actual.
     */
    public Collection getActualPage(){
        //colección donde se devolverán los datos
        Vector pagina=new Vector();
        //obtenemos un iterador sobre los datos
        Iterator it=this.datos.iterator();
        //calculamos el número de iteraciones que hay
        //que realizar para llegar a la pagina actual
        int numIteraciones=(this.pagActual-1)*this.datosPorPagina;
        //iteramos hasta posicionarnos al principio de la pagina actual
        int contador=0;
        while(it.hasNext()&&contador<numIteraciones){
            it.next();
            contador++;
        }
        //inicializamos el contador de elementos
        contador=0;
        //iteramos sobre el iterador
        while(it.hasNext()&&contador<this.datosPorPagina){
            //añadimos datos a la pagina a devolver
            pagina.add(it.next());
            contador++;
        }
        //se devuelve la pagina de datos
        return pagina;
    }
    /**
     * Obtiene la primera página de datos.ATENCIÓN: mueve el puntero
     * de página actual a la primera página.
     * @return Una colección con la primera página de datos.
     */
    public Collection getFirstPage(){
        //establecemos el número de pagina actual a la primera
        this.pagActual=1;
        //se devuelve la pagina de datos
        return this.getActualPage();
    }
    /**
     * Obtiene la última página de datos. ATENCIÓN: mueve el puntero
     * de página actual a la última página.
     * @return Una colección con la última página de datos.
     */
    public Collection getLastPage(){
        //establecemos el número de pagina actual a la ultima
        this.pagActual=this.numPaginas;
        //se devuelve la pagina de datos
        return this.getActualPage();
    }
    /**
     * Obtiene la página de datos anterior. ATENCIÓN: mueve el puntero
     * de página actual a la página anterior.
     * @return Una colección con la página de datos anterior. Si estamos en
     * la primera página devuelve la primera página.
     */
    public Collection getPrevPage(){
        //comprobamos si estamos en la primera antes de retrasar el puntero
        //de página
        if(!isFirstPage()) this.pagActual--;
        //se devuelve la pagina de datos
        return this.getActualPage();
    }
    /**
     * Obtiene la página de datos siguiente. ATENCIÓN: mueve el puntero
     * de página actual a la página siguiente.
     * @return Una colección con la página de datos siguiente. Si estamos en
     * la última página devuelve la última página.
     */
    public Collection getNextPage(){
        //comprobamos si estamos en la última antes de aumentar el puntero
        //de página
        if(!isLastPage()) this.pagActual++;
        //se devuelve la pagina de datos
        return this.getActualPage();
    }
    /**
     * Actualiza los datos del paginador, el número de páginas e inicializa el 
     * puntero de paginas. Se ha de usar cuando se cambia el criterio de consulta 
     * y/o su valor y despues de haber obtenido los datos correspondientes de una
     * fuente de datos.
     * @param datos La colección con los datos actualizados.
     */
    public void actualizar(Collection datos){
        //actualizamos los datos
        this.datos=datos;
        //actualizamos el número de páginas
        this.numPaginas=datos.size()/this.datosPorPagina;
        //si el resto de la división entera es diferente de 0 sumamos 1 página
        if(datos.size()%this.datosPorPagina!=0)this.numPaginas++;
        //inicializamos el puntero de páginas
        this.pagActual=1;
    }
    
}
