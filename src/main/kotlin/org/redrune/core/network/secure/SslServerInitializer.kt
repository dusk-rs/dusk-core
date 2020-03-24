package org.redrune.core.network.secure

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.ssl.IdentityCipherSuiteFilter
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.SelfSignedCertificate

/**
 * @author Tyluur <contact@kiaira.tech>
 * @since March 18, 2020
 */
class SslServerInitializer(private val config: SslConfig) : ChannelInitializer<SocketChannel>() {

    override fun initChannel(ch: SocketChannel) {
        val signedCertificate = SelfSignedCertificate()
        val sslContext = SslContextBuilder.forServer(config.certificationFile, config.keyFile).apply {
            trustManager((if (config.trustCertificationFile.exists()) config.trustCertificationFile else null))
            keyManager(config.certificationFile, config.keyFile)
            ciphers(null, IdentityCipherSuiteFilter.INSTANCE)
            sessionCacheSize(0)
            sessionTimeout(0)
        }.build()
        ch.pipeline().addLast("ssl.handler", sslContext.newHandler(ch.alloc()))
    }

}