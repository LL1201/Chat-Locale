## Loner FTP Server
## Introduzione

Loner FTP server è un software Open Source multipiattaforma che grazie ad una semplice interfaccia grafica permette di avviare un server FTP accessibile localmente e da remoto. I comandi implementati sono quelli base del protocollo FTP, ed è supportata la modalità passiva ed attiva del server, a discrezione del client. I protocolli di crittografia non sono supportati, tutto il traffico passa in chiaro sulla rete. È inoltre stata implementata una semplice gestione degli utenti: l’utente anonimo “anonymous” è già configurato di default e permette solamente la lettura delle risorse; gli altri utenti che vengono configurati dall’utilizzatore possiedono una password e, oltre ai permessi di lettura, possono scrivere e modificare le risorse file condivise dal server.

Comandi supportati:
- USER
- PASS
- LIST
- RETR
- PASV
- EPSV
- PWD
- CWD
- CDUP
- STOR 
- MKD
- RMD
- DELE
- PORT
- RNFR
- RNTO

## Caratteristiche tecniche generali

- **Tecnologia:** Java SE-1.8
- **Funzionalità principali:** Supporto IPv4, trasferimento file alla massima velocità della rete, compatibilità con i più comuni client FTP in commercio.
- **Tipo di progetto:** Server con interfaccia grafica
- **Specifiche algoritmi utilizzati:** Algoritmo per listare i file in una directory: ciclo for each in grado, attraverso gli opportuni controlli, di rispondere al client con una lista di tutti i file contenuti nella directory richiesta inserendo permessi, dimensione, data dell’ultima modifica e nome.
Algoritmo per il trasferimento di file: per l’invio dei file viene letta la dimensione del file e viene creato un buffer sul quale vengono scritti i byte che sono inviati al client. Per la ricezione dei file viene aperto un buffer che si occupa di acquisire i byte inviati dal client e successivamente scriverli in memoria.
- **Ambiente di sviluppo:** Visual Studio Code, Eclipse IDE
- **Dispositivi e sistemi operativi supportati:** Computer desktop, laptop che rispetti i requisiti minimi:
- **Sistema operativo (x86 o x64):** Qualsiasi sistema che supporti Java Virtual Machine 1.8 
- **RAM:** 256 MB
- **Spazio disco disponibile:** 150 MB
- **Connettività ad internet:** Connettività Internet necessaria solo se è necessario accedere da remoto al server FTP
- **Software di terze parti:** Java Virtual Machine 1.8
