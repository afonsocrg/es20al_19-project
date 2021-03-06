#!/bin/ash

set -e # fail fast

# check that required env vars exist
assert_present() {
	local var="$1"
	if eval 'test -z "${'"$var"'}"'; then
		echo "Variable $var not set"
		exit 1
	fi
}
assert_present PORT

cat > /etc/nginx/nginx.conf <<EOF
user nginx;
worker_processes auto;

pid /var/run/nginx.pid;
error_log /var/log/nginx/error.log warn;

events {
	worker_connections 1024;
}

http {
	include /etc/nginx/mime.types;
	default_type application/octet-stream;

	log_format main '\$remote_addr - \$remote_user [\$time_local] "\$request" '
		'\$status \$body_bytes_sent "\$http_referer" '
		'"\$http_user_agent" "\$http_x_forwarded_for"';

	access_log /var/log/nginx/access.log main;

	sendfile on;
	tcp_nopush on;
	tcp_nodelay on;
	keepalive_timeout 65;

	gzip on;
	gzip_static on;
	gzip_types text/plain text/css application/json application/x-javascript text/xml application/xml application/xml+rss text/javascript;
	gzip_proxied any;
	gzip_vary on;
	gzip_comp_level 6;
	gzip_buffers 16 8k;
	gzip_http_version 1.1;

	server {
		listen $PORT default_server;
		listen [::]:$PORT default_server;

		location / {
			root /app;
			index index.html;
			try_files \$uri \$uri/ /index.html;
		}

		error_page 500 502 503 504 /50x.html;
		location = /50x.html {
			root /usr/share/nginx/html;
		}
	}
}
EOF

exec nginx -g "daemon off;"
