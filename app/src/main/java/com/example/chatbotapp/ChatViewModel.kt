//package com.example.chatbotapp
//
//import android.util.Log
//import androidx.compose.runtime.mutableStateListOf
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.launch
//import com.google.ai.client.generativeai.GenerativeModel
//import com.google.ai.client.generativeai.type.content
//import kotlin.collections.removeLast
//
//
//
//class ChatViewModel : ViewModel() {
//    companion object {
//        val apiKey = "AIzaSyCVbSvwLjfERPbWsuCDXh_ioo0YXB0MIFk" // companion object
//    }
//
//    val messageList by lazy {
//        mutableStateListOf<MessageModel>()
//    }
//
//    private val generativeModel: GenerativeModel = GenerativeModel(
//        modelName = "gemini-1.5-pro",
////        apiKey = Constants.apiKey
//        apiKey = apiKey
//    )
//
//    fun sendMessage(question: String) {
//        viewModelScope.launch { // coroutine
//
//                try {
//                    val chat = generativeModel.startChat(
//                        history = messageList.map {   // Higher-Order Function and Lambdas
//                            content(it.role){text(it.message)} // type reference
//                        }.toList()
//                    )
//
//                    messageList.add(MessageModel(question, "user")) // type inference
//                    messageList.add(MessageModel("Typing....", "model"))
//
//                    val response = chat.sendMessage(question)
//                    // Remove the last item manually
//                    if (messageList.isNotEmpty()) {
//                        messageList.removeAt(messageList.size - 1)
//                    }
//                    messageList.add(MessageModel(response.text.toString(), "model"))
//                } catch (e : Exception) {
//                    // Remove the last item manually
//                    if (messageList.isNotEmpty()) {
//                        messageList.removeAt(messageList.size - 1)
//                    }
//                    messageList.add(MessageModel("Error : "+e.message.toString(), "model")) // null safety
//                }
//
//        }
//    }
//}

package com.example.chatbotapp

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlin.collections.removeLast

// Interface defining chatbot operations
interface ChatOperations {
    fun sendMessage(question: String)
}

// Abstract base class for shared functionality
abstract class BaseChatViewModel : ViewModel(), ChatOperations {
    abstract val messageList: MutableList<MessageModel>
}

class ChatViewModel : BaseChatViewModel() {
    companion object {
        val apiKey = "AIzaSyCVbSvwLjfERPbWsuCDXh_ioo0YXB0MIFk"
    }

    override val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }

    private val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-1.5-pro",
//        apiKey = Constants.apiKey
        apiKey = apiKey
    )

    override fun sendMessage(question: String) {
        viewModelScope.launch { // coroutines

            try {
                val chat = generativeModel.startChat(
                    history = messageList.map {
                        it  as MessageModel // type casting
                        content(it.role){text(it.message)} // type inference
                    }.toList()
                )

                messageList.add(MessageModel(question, "user"))
                messageList.add(MessageModel("Typing....", "model"))

                val response = chat.sendMessage(question)
                // Remove the last item manually
                if (messageList.isNotEmpty()) {
                    messageList.removeAt(messageList.size - 1)
                }
                messageList.add(MessageModel(response.text.toString(), "model"))
            } catch (e : Exception) {
                // Remove the last item manually
                if (messageList.isNotEmpty()) {
                    messageList.removeAt(messageList.size - 1)
                }
                messageList.add(MessageModel("Error : "+e.message.toString(), "model"))
            }

        }
    }
}
