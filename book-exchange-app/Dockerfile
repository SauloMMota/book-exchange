FROM node:lts-alpine as angular
WORKDIR /app
COPY package.json /app
RUN npm install --silent
COPY . .
RUN npm run build --prod

FROM nginx:alpine
VOLUME /var/cache/nginx
COPY --from=angular /app/dist/book-exchange-app /usr/share/nginx/html
COPY ./config/nginx.conf /etc/nginx/conf.d/default.conf

# docker build -t book-exchange-app .
# docker run -p 4200:80 book-exchange-app
