package com.prasunmondal.mbros_delivery.utils.mailUtils

import java.util.*
import javax.activation.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart


/**
 * Created by brandonjenniges on 11/6/15.
 */
class Mail() : Authenticator() {
    private var _user = ""
    private var _pass = ""
    lateinit private var _to: Array<String>
    private var _from = ""
    private var _port = "465"
    private var _sport = "465"
    private var _host = "smtp.gmail.com"
    private var _subject = ""
    private var isHTML = true

    // the getters and setters
    var body = ""
    private var _auth = true
    private var _debuggable = false
    private var _multipart: Multipart

    constructor(user: String, pass: String) : this() {
        _user = user
        _pass = pass
    }

    @Throws(Exception::class)
    fun send(): Boolean {
        val props = _setProperties()
        return if (_user != "" && _pass != "" && _to.size > 0 && _from != "" && _subject != ""
            && body != ""
        ) {
            val session = Session.getInstance(props, this)
            val msg = MimeMessage(session)
            msg.setFrom(InternetAddress(_from))
            val addressTo = arrayOfNulls<InternetAddress>(_to.size)
            for (i in _to.indices) {
                addressTo[i] = InternetAddress(_to[i])
            }
            msg.setRecipients(MimeMessage.RecipientType.TO, addressTo)
            msg.subject = _subject
            msg.sentDate = Date()

            // setup message body
            val messageBodyPart: BodyPart = MimeBodyPart()
            messageBodyPart.setText(body)
            _multipart.addBodyPart(messageBodyPart)
            msg.setHeader("X-Priority", "1")
            // Put parts in message
            if(isHTML) {
                println("You have sent an html string")
                msg.setContent(body, "text/html; charset=utf-8")
            }
            else {
                println("You have sent an non html string")
                msg.setContent(_multipart)
            }
            // send email
            Transport.send(msg)
            true
        } else {
            false
        }
    }

    @Throws(Exception::class)
    fun addAttachment(filename: String?) {
        val messageBodyPart: BodyPart = MimeBodyPart()
        val source: DataSource = FileDataSource(filename)
        messageBodyPart.dataHandler = DataHandler(source)
        messageBodyPart.fileName = filename
        _multipart.addBodyPart(messageBodyPart)
    }

    public override fun getPasswordAuthentication(): PasswordAuthentication {
        return PasswordAuthentication(_user, _pass)
    }

    private fun _setProperties(): Properties {
        val props = Properties()
        props["mail.smtp.host"] = _host
        if (_debuggable) {
            props["mail.debug"] = "true"
        }
        if (_auth) {
            props["mail.smtp.auth"] = "true"
        }
        props["mail.smtp.port"] = _port
        props["mail.smtp.socketFactory.port"] = _sport
        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        props["mail.smtp.socketFactory.fallback"] = "false"
        return props
    }

    fun get_user(): String {
        return _user
    }

    fun set_user(_user: String) {
        this._user = _user
    }

    fun get_pass(): String {
        return _pass
    }

    fun set_pass(_pass: String) {
        this._pass = _pass
    }

    fun get_to(): Array<String> {
        return _to
    }

    fun set_to(_to: Array<String>) {
        this._to = _to
    }

    fun get_from(): String {
        return _from
    }

    fun set_from(_from: String) {
        this._from = _from
    }

    fun get_port(): String {
        return _port
    }

    fun set_port(_port: String) {
        this._port = _port
    }

    fun get_sport(): String {
        return _sport
    }

    fun set_sport(_sport: String) {
        this._sport = _sport
    }

    fun get_host(): String {
        return _host
    }

    fun set_host(_host: String) {
        this._host = _host
    }

    fun get_subject(): String {
        return _subject
    }

    fun set_subject(_subject: String) {
        this._subject = _subject
    }

    fun setIsHTML(isHTML: Boolean) {
        this.isHTML = isHTML
    }

    fun is_auth(): Boolean {
        return _auth
    }

    fun set_auth(_auth: Boolean) {
        this._auth = _auth
    }

    fun is_debuggable(): Boolean {
        return _debuggable
    }

    fun set_debuggable(_debuggable: Boolean) {
        this._debuggable = _debuggable
    }

    fun get_multipart(): Multipart {
        return _multipart
    }

    fun set_multipart(_multipart: Multipart) {
        this._multipart = _multipart
    }

    init {
        // default smtp server
        // default smtp port
        // default socketfactory port
        // username
        // password
        // email sent from
        // email subject
        // email body
        // debug mode on or off - default off
        // smtp authentication - default on
        _multipart = MimeMultipart()

        // There is something wrong with MailCap, javamail can not find a
        // handler for the multipart/mixed part, so this bit needs to be added.
        val mc = CommandMap
            .getDefaultCommandMap() as MailcapCommandMap
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html")
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml")
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain")
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed")
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822")
        CommandMap.setDefaultCommandMap(mc)
    }
}