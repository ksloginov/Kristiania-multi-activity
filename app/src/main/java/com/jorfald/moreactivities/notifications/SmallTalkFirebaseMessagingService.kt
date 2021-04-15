package com.jorfald.moreactivities.notifications

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject

class SmallTalkFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        subscribeDeviceTokenToPushNotifications(
            applicationContext,
            token
        )
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title
        val content = message.notification?.body

        Log.d("FirebaseMessagingService", "Received a message. Title: $title ; Body: $content")

        message.data["user_id"]?.let {
            // Go directly to the User Page, for example
        }

        // By now - just refresh a Chat Fragment
        notificationReceivedListener.notificationReceived()
    }

    companion object {

        var notificationReceivedListener =
            NotificationReceivedListener()

        fun subscribeDeviceTokenToPushNotifications(context: Context, token: String) {
            val url = "https://us-central1-smalltalk-3bfb8.cloudfunctions.net/api/registerUserToken"
            Volley.newRequestQueue(context).add(JsonObjectRequest(
                Request.Method.POST,
                url,
                JSONObject("{$token}"),
                {
                    Log.d("Notification Service", "Posted Device Token to server")
                },
                { _ ->
                    Log.d("Notification Service", "Failed to post Device Token")
                }
            ))
        }

    }
}