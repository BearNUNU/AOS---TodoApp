const express = require('express');
const router = express.Router();
const User = require('../model/User');
const jwt = require('jsonwebtoken');
require('dotenv').config();
const jwtSecret = process.env.JWT_SECRET;

router.post('/register', async (req, res) => {
    const { id, pw } = req.body;

    if (!id || !pw) {
        return res.status(400).json({ message: '아이디와 비밀번호를 입력해야 합니다.' });
    }

    try {
        const user = new User({ id, pw }); 
        await user.save(); 
        res.status(201).json({ message: '회원가입 성공' });
    } catch (err) {
        console.error(err);
        res.status(500).json({ message: 'id와 pw를 확인하세요' });
    }
});

router.post('/login', async (req, res) => {
    const { id, pw } = req.body;

    if (!id || !pw) {
        return res.status(400).json({ message: '아이디와 비밀번호를 입력해야 합니다.' });
    }

    try {
        const user = await User.findOne({ id }); 
        if (!user) {
            return res.status(401).json({ message: '아이디를 확인하세요' });
        }

        const isMatch = await user.comparePassword(pw); 
        if (!isMatch) {
            return res.status(401).json({ message: '비밀번호를 확인하세요' });
        }

        const token = jwt.sign({ id: user._id, id: user.id },jwtSecret, { expiresIn: '30d' }); 
        res.json({ token });
    } catch (err) {
        console.error(err);
        res.status(500).json({ message: '서버 오류' });
    }
});


router.post('/verifyJWT', async(req, res) => {
    const token = req.body.token;

    if (!token) {
        return res.status(400).json({ message: '토큰이 없음' });
    }

    jwt.verify(token, jwtSecret, (err, decoded) => {
        if (err) {
            return res.status(401).json({ message: '유효하지 않은 토큰' });
        }
        res.status(200).json({ message: '로그인 성공', userId: decoded.id });
    });
});


module.exports = router;
