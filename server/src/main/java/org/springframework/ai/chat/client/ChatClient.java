package org.springframework.ai.chat.client;

/*
 * Omdat er op dit moment geen mogelijkheid voor AI-testing is zonder flink te moeten betalen,
 * maar de dependency vereist dat er een connectie met OpenAI gemaakt wordt.
 * Daarom is heb ik dit NoOpInterface toegevoegd, zodat ik in mijn testen de nog wel kan mocken.
 */

import org.springframework.lang.Nullable;

public interface ChatClient {

    ChatClientRequestSpec prompt(String content);

    interface CallResponseSpec {

        @Nullable
        String content();
    }

    interface ChatClientRequestSpec {

        CallResponseSpec call();
    }
}
