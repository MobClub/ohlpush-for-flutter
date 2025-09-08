
class OHLPushCustomMessage {
	String content;
	String? messageId;
	int? timestamp;
	Map<String, dynamic>? extrasMap;

	OHLPushCustomMessage(this.content, this.messageId, this.timestamp, this.extrasMap);

	factory OHLPushCustomMessage.fromJson(Map<String, dynamic> json) {
		return OHLPushCustomMessage(json['content'], json['messageId'], json['timeStamp'], json['extrasMap']);
	}

}