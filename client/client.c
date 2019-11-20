#include <curses.h>
#include <ncurses.h>
#include <stdio.h>
#include <string.h>
#include <signal.h>
#include <unistd.h>
#include <sys/socket.h>
#include <arpa/inet.h>

#define IP "127.0.0.1"
#define PORT 8888
#define RET_LEN 2048

char prompt[] = " > ";
char test[] = "the line olala";

void	init_server(struct sockaddr_in *server)
{
	server->sin_addr.s_addr = inet_addr(IP);
	server->sin_family = AF_INET;
	server->sin_port = htons(PORT);
}

int		init_socket()
{
	int					sock;
	struct sockaddr_in	server;
	
	// Create socket
	sock = socket(AF_INET, SOCK_STREAM, 0);
	if (sock == -1)
	{
		printf("Could not create the socket.\n");
		return (-1);
	}
	init_server(&server);

	// Connect to remote server
	if (connect(sock, (struct sockaddr *)&server, sizeof(server)) < 0)
	{
		printf("Connection %s:%d to failed.\n", IP, PORT);
		return (-1);
	}
	
	return (sock);
}

int		get_response(int sock, char *ret)
{
	if (recv(sock, ret, RET_LEN, 0) < 0)
	{
		ret = "Error recieving server's response.\n";
		return (-1);
	}
	return (0);
}

int		send_message(int sock, char *msg, char *ret)
{
	// Sending the message to the server
	if (send(sock, msg, strlen(msg), 0) < 0)
	{
		ret = "Error sending the message to the server.\n";
		return (-1);
	}

	// Recieving server's response
	return get_response(sock, ret);
}

int		handle_builtin(char *str, int *x, int *y, int sock, char *ret)
{
	char tmp[strlen(str) + 9];

	strcpy(tmp, "builtin@");
	strcat(tmp, str);
	strcat(tmp, "\n");
	if (!strcmp("clear", str))
	{
		send_message(sock, tmp, ret);
		clear();
		*y = 0;
		return (1);
	}
	return (0);
}

int		count_chars(char *s, char c)
{
	int i;

	for (i = 0; s[i]; s[i] == c ? i++ : *s++);
	return (i);
}

int		main(int ac, char **av)
{
	// Prompt stuff
	int		c;
	int		x;
	int		max_x;
	int		min_x;
	int		y;
	int		max_y;
	// Socket stuff
	int		sock;
	char	ret[RET_LEN + 1];

	// Initializing socket
	sock = init_socket();
	// Handling socket initialization error
	if (sock == -1) return (0);
	// Initializing ncurses for prompt
	initscr();
	keypad(stdscr, 1);
	scrollok(stdscr, 1);
	timeout(-1);
	noecho();
	min_x = strlen(prompt);
	y = 0;

	while (1)
	{
		printw(prompt);
		x = min_x;
		max_x = min_x;
		max_y = getmaxy(stdscr);
		while ((c = getch()) != 10)
		{
			char tmp[max_x + 1];
			switch (c)
			{
				case KEY_UP:
					bzero(ret, RET_LEN + 1);
					send_message(sock, "history@up\n", ret);
					mvaddstr(y, min_x, ret);
					x = min_x + strlen(ret) - 1;
					clrtoeol();
					move(y, x);
					break;
				case KEY_DOWN:
					bzero(ret, RET_LEN + 1);
					send_message(sock, "history@down\n", ret);
					mvaddstr(y, min_x, ret);
					x = min_x + strlen(ret) - 1;
					clrtoeol();
					move(y, x);
					break;
				case KEY_LEFT:
					if (min_x != x) x--;
					move(y, x);
					break;
				case KEY_RIGHT:
					if (max_x != x) x++;
					move(y, x);
					break;
				case KEY_HOME:
					x = min_x;
					move(y, x);
					break;
				case KEY_END:
					x = max_x;
					move(y, x);
					break;
				case KEY_DC:
					if (min_x <= x && x != max_x)
					{
						mvdelch(y, x);
						max_x--;
					}
					break;
				case 127:
					if (min_x != x)
					{
						x--;
						max_x--;
						mvdelch(y, x);
					}
					break;
				case 12:
					mvinnstr(y, 0, tmp, max_x);
					clear();
					y = 0;
					printw("%s", tmp);
					move(y, x);
					break;
				default:
					addch(c);
					x++;
					break;
			}
			if (max_x < x) max_x = x;
		}
		// Handling empty strings
		if (max_x == min_x)
		{
			addch('\n');
			if (y + 1 < max_y) y++;
			continue;
		}
		// Reading current line data and moving to current index
		char str[max_x];
		mvinnstr(y, min_x, str, max_x - min_x);
		move(y, x);
		// Adding the newline asked and incrementing lines nbr
		addch('\n');
		if (y + 1 < max_y) y++;
		// Handling builtins locally
		if (handle_builtin(str, &x, &y, sock, ret))
			continue;
		// Formating str and ret for send_message
		str[max_x - min_x] = '\n';
		str[max_x - min_x + 1] = 0;
		bzero(ret, RET_LEN + 1);
		int r = send_message(sock, str, ret);
		// Handling send error
		if (r == -1)
		{
			printf("%s", ret);
			break;
		}
		// Printing server response
		addstr(ret);
		y += count_chars(ret, '\n');
		if (y >= max_y) y = max_y - 1;
	}
	endwin();
	close(sock);
	return (0);
}
