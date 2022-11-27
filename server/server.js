const express = require('express'); //requires express module
const socket = require('socket.io'); //requires socket.io module
const fs = require('fs');
const app = express();
var PORT = process.env.PORT || 3000;
const server = app.listen(PORT); //tells to host server on localhost:3000


//Playing variables:
app.use(express.static('public')); //show static files in 'public' directory
console.log('Server is running');
const io = socket(server);

var count = 0;


//Socket.io Connection------------------
io.on('connection', (socket) => {

    console.log("New socket connection id: " + socket.id)

    socket.on('new message', (msg) => {
        //TODO: 
        console.log("New message received: "+msg)
        socket.broadcast.emit("receive message", msg)
    })

    socket.on('disconnect', function() {
        console.log( 'user '+socket.id+' has left ')
    });
})