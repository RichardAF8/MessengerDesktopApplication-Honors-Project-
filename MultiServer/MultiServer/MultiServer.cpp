#include <iostream>
#include <WS2tcpip.h>
#include <sstream>
using namespace std;

//instruction to compiler for windows compiler to include this library
#pragma comment(lib, "ws2_32.lib")

void main()
{
	//Initilazie winsock
	WSADATA winsock;

	WORD version = MAKEWORD(2, 3);

	int call = WSAStartup(version, &winsock);

	if (call != 0) {
		//explain cerr
		cerr << "You Done Fucked Up";
		return;
	}
	//Create Socket
	//what is a flag
	SOCKET listener = socket(AF_INET, SOCK_STREAM, 0);
	if (listener == INVALID_SOCKET) {
		cerr << "You Done Fucked Up";
		return;

	}
	//bind
	sockaddr_in hint;
	hint.sin_family = AF_INET;
	hint.sin_port = htons(85);
	hint.sin_addr.S_un.S_addr = INADDR_ANY;

	bind(listener, (sockaddr*)&hint, sizeof(hint));

	//tells winsokc that this socket is the server listening
	listen(listener, SOMAXCONN);
	//a struct containing all the file descriptors for the sockets  in a set
	fd_set master;
	FD_ZERO(&master);
	//adds t0 the set
	FD_SET(listener, &master);

	while (true) {

		fd_set copy = master;
		//?? creates a queue for the client
		int socketCount = select(0, &copy, nullptr, nullptr, nullptr);

		for(int i=0; i<socketCount; i++)
		{
		
			SOCKET sock = copy.fd_array[i];
			if(sock==listener)
			{
			// accepts new connection
				SOCKET client = accept(listener, nullptr, nullptr);
				// add the new connection to the list of connected clients
				FD_SET(client, &master);
				cerr << "Client Connected"<< endl;

				//Send Welcome message
				string welcome = "\nWelcome to the Chat\n";
				send(client, welcome.c_str(), welcome.size() + 1, 0);
			}
			else {

				char message[4096];
				ZeroMemory(message, 4096);
				int bytesRrecieved = recv(sock, message, 4096, 0);
				if (bytesRrecieved == SOCKET_ERROR)
				{
					cerr << "\nError in recv(). Quitting\n" << endl;
					closesocket(sock);
					FD_CLR(sock, &master);
					break;

				}
				if (bytesRrecieved <= 0)
				{
					cout << "\nClient Disconnected\n" << endl;
					closesocket(sock);
					FD_CLR(sock, &master);

				}
				else {
					//send message to other clients
					for (int i = 0; i < master.fd_count;i++) {

						SOCKET outSock = master.fd_array[i];
						if(outSock!= listener && outSock != sock)
						{
							ostringstream ss;
							ss << "\nUser#" << sock << " :\n" << message;
							string out = ss.str();

						
							send(outSock, out.c_str(), out.size()+1, 0);
						}



					}


				}
			}
		
		
		}


	}



	//CLEAN
	WSACleanup();

	system("pause");
    
}

