/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios;

import dominio.Pedido;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author Alvaro
 */
public class GestorPedido {
    /**
     * Esta es la version del gestor de pedidos del restaurante, funciona de forma
     * diferente que la del cliente
     * Esta vesion se encarga de gestionar todos los pedidos que hacen
     * los distintos clientes
     */
    private final Collection<Pedido> pendientes; //Pedidos confirmados, los que se pagaran
    
    public GestorPedido() {
        this.pendientes = new HashSet();
    }
    
    //Añadir, quitar y limpiar sets
    public Collection<Pedido> getPendientes(){
        return this.pendientes;
    }
    public void addToPendientes(Pedido pedido){
        this.pendientes.add(pedido);
    }
    public void removeFromPendientes(Pedido pedido){
        //No se deberia poder, solo lo puede hacer el restaurante/admin
        this.pendientes.remove(pedido);
    }
    
    public void clearPendientes(){
        //Tampoco se deberia poder
        this.pendientes.clear();
    }
    
    public void pagarPendiente(Pedido pedido){
        if(pedido != null && pendientes.contains(pedido)){
            this.pendientes.remove(pedido);
        }
    }
    
    public void pagarPendientes(Collection<Pedido> pedidos){
        if(pedidos != null && pendientes.containsAll(pedidos)){
            this.pendientes.removeAll(pedidos);
        }
    }
    
    public void removeFromPagados(Pedido pedido){
        //No se deberia poder, solo lo puede hacer el restaurante/admin
        this.pendientes.remove(pedido);
    }
    
    public void clearPagados(){
        //Tampoco se deberia poder
        this.pendientes.clear();
    }
    
    
}
