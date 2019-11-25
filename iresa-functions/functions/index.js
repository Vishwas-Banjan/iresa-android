const functions = require('firebase-functions');
const admin = require('firebase-admin');
const express = require('express');
admin.initializeApp();


exports.updateUpvoteCount = functions.firestore
    .document('OrderedList/{documentId}')
    .onWrite((change, context) => {
        const newDocument = change.after.data();
        const oldDocument = change.before.data();
        if (newDocument.upvotes.length == oldDocument.upvotes.length) return null;
        let count = newDocument.upvotes.length;
        if (!count) {
            count = 0;
        }
        return change.after.ref.set({
            upvoteCount: count
        }, {merge: true});
    });
