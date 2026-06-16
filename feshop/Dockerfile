FROM node:16-alpine AS build
WORKDIR /app

ARG REACT_APP_API_BASE_URL=/api/
ENV REACT_APP_API_BASE_URL=${REACT_APP_API_BASE_URL}

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build

FROM nginx:1.25-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
