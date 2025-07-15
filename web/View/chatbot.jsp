<%-- 
    Document   : chatbot
    Created on : Jul 10, 2025, 10:03:44 PM
    Author     : HieuTT
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<style>
    #gemini-widget {
        position: fixed;
        bottom: 20px;
        right: 20px;
        z-index: 9999;
        font-family: Arial;
    }

    #gemini-widget #gemini-toggle {
        width: 50px;
        height: 50px;
        background-color: #4285f4;
        color: white;
        font-size: 24px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
    }

    #gemini-widget #gemini-popup {
        display: none;
        flex-direction: column;
        width: 350px;
        height: 500px;
        background: white;
        border: 1px solid #ccc;
        border-radius: 10px;
        box-shadow: 0 0 15px rgba(0, 0, 0, 0.3);
        overflow: hidden;
    }

    #gemini-widget #gemini-popup.open {
        display: flex;
    }

    #gemini-widget .header-chat {
        background: #4285f4;
        color: white;
        padding: 10px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        font-weight: bold;
    }

    #gemini-widget .chat-box {
        flex: 1;
        display: flex;
        flex-direction: column;
        padding: 10px;
        overflow-y: auto;
        border-top: 1px solid #ccc;
        border-bottom: 1px solid #ccc;
    }

    #gemini-widget .bubble {
        padding: 10px 15px;
        border-radius: 10px;
        margin: 6px 0;
        max-width: 80%;
        white-space: pre-wrap;
        position: relative;
    }

    #gemini-widget .user {
        background-color: #DCF8C6;
        align-self: flex-end;
    }

    #gemini-widget .bot {
        background-color: #F1F0F0;
        align-self: flex-start;
    }

    #gemini-widget .timestamp {
        font-size: 10px;
        color: #888;
        right: 10px;
        max-width: 180px;
    }

    #gemini-widget textarea {
        resize: none;
        border: none;
        padding: 8px;
        width: calc(100% - 16px);
    }

    #gemini-widget .controls {
        display: flex;
        justify-content: space-between;
        padding: 5px 10px;
    }

    #gemini-widget button {
        cursor: pointer;
        padding: 5px 10px;
    }
</style>
<div id="gemini-widget">
    <div id="gemini-toggle" onclick="toggleChat()"><i class="bi bi-chat-fill"></i></div>

    <div id="gemini-popup">
        <div class="header-chat">
            <span>Chatbot AI</span>
            <button onclick="toggleChat()"><i class="bi bi-x-lg"></i></button>
        </div>
        <div id="chatBox" class="chat-box"></div>
        <textarea id="question" rows="3" placeholder="Nhập câu hỏi..."></textarea>
        <div class="controls">
            <button onclick="sendQuestion()">Gửi</button>
            <button onclick="clearChat()"><i class="bi bi-trash3"></i></button>
        </div>
    </div>
</div>

<script>
    let isWaitingForBot = false;
    function toggleChat() {
        const popup = document.getElementById("gemini-popup");
        popup.classList.toggle("open");
    }

    async function sendQuestion() {
        if (isWaitingForBot)
            return;
        const questionEl = document.getElementById("question");
        const question = questionEl.value.trim();
        if (!question)
            return;

        appendBubble(question, "user", new Date().toLocaleString());
        questionEl.value = "";
        isWaitingForBot = true;

        try {
            const res = await fetch("chat", {
                method: "POST",
                headers: {"Content-Type": "application/x-www-form-urlencoded"},
                body: "question=" + encodeURIComponent(question)
            });
            const data = await res.json();
            appendBubble(data.reply, "bot", new Date().toLocaleString());
        } catch (e) {
            appendBubble("Lỗi: " + e.message, "bot", new Date().toLocaleString());
        }
    }

    document.addEventListener("DOMContentLoaded", () => {
        fetch("chat")
                .then(res => res.json())
                .then(history => {
                    history.forEach(entry => {
                        appendBubble(entry.text, entry.type, entry.time);
                    });
                });

        const textarea = document.getElementById("question");
        textarea.addEventListener("keydown", function (e) {
            if (e.key === "Enter" && !e.shiftKey) {
                e.preventDefault();
                sendQuestion();
            }
        });
    });

    function appendBubble(text, type, time) {
        if (type === "bot") {
            isWaitingForBot = false;
        }
        const chatBox = document.getElementById("chatBox");

        const bubbleWrapper = document.createElement("div");
        bubbleWrapper.style.display = "flex";
        bubbleWrapper.style.alignItems = "flex-end";
        bubbleWrapper.style.marginBottom = "6px";
        bubbleWrapper.style.justifyContent = type === "user" ? "flex-end" : "flex-start";

        const avatar = document.createElement("img");
        avatar.src = type === "user"
                ? "https://cdn-icons-png.flaticon.com/512/847/847969.png"
                : "https://cdn-icons-png.flaticon.com/512/4712/4712034.png";
        avatar.alt = type;
        avatar.style.width = "30px";
        avatar.style.height = "30px";
        avatar.style.borderRadius = "50%";
        avatar.style.margin = type === "user" ? "0 0 14px 8px" : "0 8px 45px 0";

        const bubble = document.createElement("div");
        bubble.className = `bubble ${type}`;
        bubble.style.display = "flex";
        bubble.style.flexDirection = "column";

        const messageText = document.createElement("div");
        messageText.innerHTML = text;
        messageText.style.wordWrap = "break-word";

        const ts = document.createElement("div");
        ts.className = "timestamp";
        ts.style.marginTop = "4px";
        ts.style.alignSelf = type === "user" ? "flex-end" : "flex-start";
        ts.textContent = time || "";

        bubble.appendChild(messageText);
        bubble.appendChild(ts);

        if (type === "user") {
            bubbleWrapper.appendChild(bubble);
            bubbleWrapper.appendChild(avatar);
        } else {
            bubbleWrapper.appendChild(avatar);
            bubbleWrapper.appendChild(bubble);
        }

        chatBox.appendChild(bubbleWrapper);
        chatBox.scrollTop = chatBox.scrollHeight;

    }

    function clearChat() {
        fetch("chat", {method: "DELETE"})
                .then(() => {
                    const chatBox = document.getElementById("chatBox");
                    chatBox.innerHTML = "";
                });
    }
</script>
