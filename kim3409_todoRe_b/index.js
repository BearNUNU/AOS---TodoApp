const mongoose = require("mongoose");

const connect = () => {
    if(process.env.NODE_ENV !== 'production') {
        mongoose.set('debug', true);
    }
    mongoose.connect('mongodb://kim:a12345@localhost:27017/admin', {
        dbName: 'todo', 
        useNewUrlParser: true,
        useUnifiedTopology: true
    })
    .then(() => console.log('MongoDB Connected...'))
    .catch((err) => console.error('MongoDB Connection Error', err));
};

mongoose.connection.on('error', (error) => {
    console.error('MongoDB Connection Error', error);
});

mongoose.connection.on('disconnected', () => {
    console.error('MongoDB Disconnected...');
    connect();
});

module.exports = connect;
