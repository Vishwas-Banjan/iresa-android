const functions = require('firebase-functions');
const admin = require('firebase-admin');
const express = require('express');
admin.initializeApp();


exports.updateUpvoteCount = functions.firestore
    .document('stations/{storeId}/songList/{songId}')
    .onWrite((change, context) => {
        if (!change.after.exists) {
            return null;
        }
        const newDocument = change.after.data();
        let count;
        if (newDocument.upvotes == undefined) {
            count = 0;
        } else {
            count = newDocument.upvotes.length;
        }
        return change.after.ref.set({
            upvoteCount: count
        }, {merge: true});
    });
