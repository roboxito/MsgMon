# MsgMon
Servidor de envío de SMS

Al instalar la app Android por primera vez, vaya a configuracion de la app y active el permiso de envio de SMS
Puede usarse el siguiente cliente de socket tcp-ip en python para enviar mensajes a una lista de numeros
Asegurese de conectar el equipo cliente y el celular Android a la misma red wifi o LAN

<code>
#python3
#eco.py

import sys
import socket

HOST="192.168.1.99"
PORT = 8099


n=len(sys.argv)
print('Argumentos: ',n)
if (n!=3):
    print('Syntaxis: eco {IP} "{Msg}"')
    exit()
HOST=sys.argv[1]
cad=b""
cad=sys.argv[2]

with socket.socket(socket.AF_INET,socket.SOCK_STREAM) as s:
    s.connect((HOST,PORT))
    s.sendall(cad.encode("ascii"))
    s.close()


# Ejemplo de sintaxis:
IP - Ip que se muestra en la pantalla de la app android
Msg- "llave,Mensaje a enviar,cel1,cel2,celn," encerrado en comillas
     
$ python eco.py 192.168.1.99 "hey_arnold$,Avisen se arma fiesta,5553200969,7773299969,4423200969," 

</code>
