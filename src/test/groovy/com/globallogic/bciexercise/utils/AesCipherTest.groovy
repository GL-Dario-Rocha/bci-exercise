package com.globallogic.bciexercise.utils

import spock.lang.Specification


class AesCipherTest extends Specification {

    Cipher cypher

    def setup() {
        cypher = new AesCipher("salt", "secret")
    }

    def "the value is the same after encrypting and decrypting"() {
        given:
            def content = "1234abc"
        when:
            def encryptedContent = cypher.encrypt(content)
        then:
            cypher.decrypt(encryptedContent) == content
    }
}
