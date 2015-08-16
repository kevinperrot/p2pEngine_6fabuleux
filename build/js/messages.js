/**
 * 
 */
var messagesList = "messagesList";
var messageDisplay = "messageDisplay";
var messagesAreLoaded = false;
var conversationsAreLoaded = false;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * 								    QUERY FROM JAVASCRIPT TO MODEL									   *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
function loadMessages(){
	if(messagesAreLoaded)
		return;
	sendQueryEmpty("loadMessages");
	messagesAreLoaded = true;
}

function loadConversation(id){
	$("#messageDisplay").empty();
	var content = {"id":id};
	sendQuery("loadConversation", content);
}
function loadConversations(){
	if(conversationsAreLoaded)
		return;
	sendQueryEmpty("loadConversations");
	conversationsAreLoaded = true;
}
function loadMessage(id){
	displayEmpty();
	var content = {"id":id};
	sendQuery("loadMessage", content);
}

function sendMessage(){
	var content = {
			subject:$("#subject").val(),
			receiver:$("#receiver").val(),
			message:$("#message").val()
	}
	sendQuery("sendMessage", content);
}

function loadSearchUsers() {
	$("#searchUsersDiv").fadeIn();
}

function searchUsers() {
	var content = {
			search:$("#searchUserInput").val()
	}
	sendQuery("searchUsers", content);
}

function removeMessage(publicKey, id) {
	var content = {
			"publicKey":publicKey,
			"id":id
	};
	sendQuery("removeMessage", content);
}

function removeConversation(id) {
	var content = {"id":id};
	sendQuery("removeConversation", content);
}
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * 								    ANSWER FROM MODEL TO JAVASCRIPT									   *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
function messagesLoaded(content) {
	$("#"+messagesList).append(newRowMessage(content));
}
function messageLoaded(content) {
	var display = getClone(messageDisplay);
	$(display).find("#senderMessage").text(content.sender);
	$(display).find("#subjectMessage").text(content.subject);
	$(display).find("#dateMessage").text(content.date);
	$(display).find("#contentMessage").append(textWithSlashNToBr(content.message));
	$("#messageDisplay").append(display);
	$("#"+removePunctuation(content.id)).removeClass("unreadedMessage");
	$("#"+removePunctuation(content.id)).addClass("readedMessage");
	if($(".unreadButton").hasClass("selected"))
		$("#"+removePunctuation(content.id)).addClass("hidden");
}
function conversationsLoaded(content) {
	$("#conversationsList tbody").append(newRowConversation(content));
}
function conversationLoaded(content) {
	var display = getClone(conversationDisplay);
	$(display).attr("id", removePunctuation(content.id));
	$(display).find("#senderMessage").text(content.sender);
	$(display).find("#subjectMessage").text(content.subject);
	$(display).find("#dateMessage").text(content.date);
	$(display).find("#contentMessage").append(textWithSlashNToBr(content.message));
	if(content.isRead === undefined){
		$(display).find(".buttonRemove").attr("onclick", "removeMessage('"+content.receiver+"','"+content.id+"');");
	} else {
		$(display).find(".buttonRemove").attr("onclick", "removeMessage('"+content.sender+"','"+content.id+"');");
	}
	$("#conversationDisplay").append(display);
	$("#"+removePunctuation(content.id)).removeClass("unreadedMessage");
	$("#"+removePunctuation(content.id)).addClass("readedMessage");
}
function messageNotSent(content) {
}
function messageRemoved(content) {
	$("#"+messagesList+" #"+removePunctuation(content.id)).detach();
	$("#conversationDisplay #"+removePunctuation(content.id)).detach();
}
function messageNotRemoved(content) {
}
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * 											HTML GENERATOR											   *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
function getWebmail(){
	loadMessages();
	loadConversations();
	return getElement(webmailForm);
}

function cancelMessage() {
	displayEmpty();
	$(".writeButton").removeClass("selected");
}

function sendMessageTo(){
	var receiver = $("#owner").text();
	var subject = $("#title").text();
	includeWebmail();
	newMessage();
	$("#receiver").val(receiver);
	$("#subject").val("Item : "+subject);
}

function newMessage() {
	if($(".writeButton").hasClass("selected"))
		cancelMessage();
	else {
		$(".writeButton").addClass("selected");
		displayEmpty();
		$("#messageDisplay").append(getClone(writeMessage));
	}
}

function newRowMessage(content){
	var row = getClone(messageRow);
	
	$(row).attr("id", removePunctuation(content.id));
	if(content.isRead === undefined)
		$(row).addClass("sentMessage hidden");
	else if(content.isRead)
		$(row).addClass("readedMessage");
	else
		$(row).addClass("unreadedMessage");
	$(row).find(".rowDate").append(content.date);
	$(row).find(".rowDate").attr("onclick", "loadMessage('"+content.id+"');");
	$(row).find(".rowSubject").append(content.subject);
	$(row).find(".rowSubject").attr("onclick", "loadMessage('"+content.id+"');");
	$(row).find(".rowFrom").append(content.sender);
	$(row).find(".rowFrom").attr("onclick", "loadMessage('"+content.id+"');");
	if(content.isRead === undefined){
		$(row).find(".buttonRemove").attr("onclick", "removeMessage('"+content.receiver+"','"+content.id+"');");
	} else {
		$(row).find(".buttonRemove").attr("onclick", "removeMessage('"+content.sender+"','"+content.id+"');");
	}
	return row;
}

function newRowConversation(content) {
	var row = getClone(conversationRow);
	$(row).attr("id", removePunctuation(content.id));
	if(!content.isRead) {
		$(row).addClass("unreadedConversation");
	}
	$(row).find(".rowDate").append(content.date);
	$(row).find(".rowDate").attr("onclick", "loadConversation('"+content.id+"');");
	$(row).find(".rowSender").append(content.sender);
	$(row).find(".rowSender").attr("onclick", "loadConversation('"+content.id+"');");
	$(row).find(".rowActions").find(".buttonRemove").attr("onclick", "removeConversation('"+content.id+"');");
	return row;
}

function displayEmpty() {
	$("#conversationDisplay").empty();
	$("#messageDisplay").empty();
}

function showSubMenu(menu) {
	var siblings = $(menu).siblings();
	for(var i = 0; i < siblings.length; i++ ) {
		if($(siblings[i]).hasClass("hidden")){
			$(siblings[i]).removeClass("hidden");
			//$(menu).addClass("selected");
			$(menu).find("img").attr("src", "./img/triangleUp.png");
		} else {
			$(siblings[i]).addClass("hidden");
			//$(menu).removeClass("selected");
			$(menu).find("img").attr("src", "./img/triangleDown.png");
		}
	}
	if($(menu).hasClass("selected"))
		showMessageReceived();
}

function showMessageReceived() {
	$("#mailbox").addClass("selected");
	$(".receivedButton").addClass("selected");
	$(".unreadButton").removeClass("selected");
	$(".sentButton").removeClass("selected");
	$(".conversationsButton").removeClass("selected");
	$("#messagesList").removeClass("hidden");
	$(".unreadedMessage").removeClass("hidden");
	$(".readedMessage").removeClass("hidden");
	$(".sentMessage").addClass("hidden");
	$("#conversationsList").addClass("hidden");
}

function showMessageUnread() {
	$("#mailbox").addClass("selected");
	$(".receivedButton").removeClass("selected");
	$(".unreadButton").addClass("selected");
	$(".sentButton").removeClass("selected");
	$(".conversationsButton").removeClass("selected");
	$("#messagesList").removeClass("hidden");
	$(".unreadedMessage").removeClass("hidden");
	$(".readedMessage").addClass("hidden");
	$(".sentMessage").addClass("hidden");
	$("#conversationsList").addClass("hidden");
}

function showMessageSent() {
	$("#mailbox").addClass("selected");
	$(".receivedButton").removeClass("selected");
	$(".unreadButton").removeClass("selected");
	$(".sentButton").addClass("selected");
	$(".conversationsButton").removeClass("selected");
	$("#messagesList").removeClass("hidden");
	$(".unreadedMessage").addClass("hidden");
	$(".readedMessage").addClass("hidden");
	$(".sentMessage").removeClass("hidden");
	$("#conversationsList").addClass("hidden");
}

function showConversations() {
	$("#mailbox").removeClass("selected");
	$(".receivedButton").removeClass("selected");
	$(".unreadButton").removeClass("selected");
	$(".sentButton").removeClass("selected");
	$(".conversationsButton").addClass("selected");
	$("#messagesList").addClass("hidden");
	$("#conversationsList").removeClass("hidden");
}