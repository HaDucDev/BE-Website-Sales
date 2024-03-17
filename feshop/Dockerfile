FROM node:16-alpine

WORKDIR /app

COPY . .

RUN npm install
FROM nginx
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
# EXPOSE 3000

# CMD ["npm", "start"]
# # Fetching the latest node image on alpine linux
# FROM node:alpine AS development

# # Declaring env
# ENV NODE_ENV development

# # Setting up the work directory
# WORKDIR /react-app

# # Installing dependencies
# COPY ./package.json /react-app
# RUN npm install

# # Copying all the files in our project
# COPY . .

# # Starting our application
# CMD npm start