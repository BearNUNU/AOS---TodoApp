const mongoose = require('mongoose');
const bcrypt = require('bcrypt');

const userSchema = new mongoose.Schema({
    id: { type: String, required: true, unique: true },
    pw: { type: String, required: true }
});

userSchema.pre('save', async function(next) {
    if (!this.isModified('pw')) return next();
    try {
        const salt = await bcrypt.genSalt(10);
        this.pw = await bcrypt.hash(this.pw, salt);
        next();
    } catch (err) {
        next(err);
    }
});

userSchema.methods.comparePassword = async function(candidatePassword) {
    return await bcrypt.compare(candidatePassword, this.pw);
};

module.exports = mongoose.model('User', userSchema);
