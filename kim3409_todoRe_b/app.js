const express = require('express');
const mongoose = require('mongoose');
const userRouter = require('./routes/android'); 
const app = express();
const PORT = 3000;
const connect = require('./index');
connect();
app.use(express.json());

app.use('/api/android', userRouter); 

app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});
