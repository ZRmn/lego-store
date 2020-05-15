FROM node:alpine
WORKDIR /static-server
ADD app app
ADD package.json .
ADD package-lock.json .
EXPOSE 4815
RUN npm install
ENTRYPOINT ["npm", "start"]