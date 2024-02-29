#python3
#eco.py

import sys
import socket

HOST="192.168.1.99"
PORT = 8099


n=len(sys.argv)
print('Argumentos: ',n)
if (n!=3):
    print('Syntaxis: eco <IP> "<Msg>"')
    exit()
HOST=sys.argv[1]
cad=b""
cad=sys.argv[2]

with socket.socket(socket.AF_INET,socket.SOCK_STREAM) as s:
    s.connect((HOST,PORT))
    s.sendall(cad.encode("ascii"))
    s.close()

# Ejemplo de sintaxis:
# IP - Ip que se muestra en la pantalla de la app android
# Msg- "<llave>,<Mensaje a enviar>, <cel1>,<cel2>,<celn>," encerrado en comillas
     
# $ python eco.py 192.168.1.99 "hey_arnold$,Avisen se arma fiesta,5553200969,7773299969,4423200969," 