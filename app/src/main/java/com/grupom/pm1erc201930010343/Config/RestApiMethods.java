package com.grupom.pm1erc201930010343.Config;

public class RestApiMethods {
    private static final String ipaddress = "transportweb2.online";
    public static final String StringHttp = "https://";
    //EndPoint Urls
    private static final String GetEmple = "/APIExamen/listacontactos.php";
    private static final String GetBuscar = "/APIExamen/listasinglecontacto.php?nombre=";
    private static final String setUpdate = "/APIExamen/actualizarcontacto.php";
    private static final String CreateUsuario = "/APIExamen/crearcontacto.php";

    //metodo get
    public static final String EndPointGetContact = StringHttp + ipaddress + GetEmple;
    public static final String EndPointGetBuscarContact = StringHttp + ipaddress + GetBuscar;
    public static final String EndPointSetUpdateContact = StringHttp + ipaddress + setUpdate;
    public static final String EndPointCreateUsuario = StringHttp + ipaddress + CreateUsuario;
}
