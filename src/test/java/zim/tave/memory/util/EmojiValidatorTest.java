package zim.tave.memory.util;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EmojiValidatorTest {

    @Test
    void testValidEmojis() {
        // 유효한 이모지들
        assertThat(EmojiValidator.isValidEmoji("🇰🇷")).isTrue(); // 대한민국
        assertThat(EmojiValidator.isValidEmoji("🇺🇸")).isTrue(); // 미국
        assertThat(EmojiValidator.isValidEmoji("🇯🇵")).isTrue(); // 일본
        assertThat(EmojiValidator.isValidEmoji("😀")).isTrue(); // 웃는 얼굴
        assertThat(EmojiValidator.isValidEmoji("🚗")).isTrue(); // 자동차
    }

    @Test
    void testInvalidEmojis() {
        // 유효하지 않은 문자열들
        assertThat(EmojiValidator.isValidEmoji("KR")).isFalse(); // 국가 코드
        assertThat(EmojiValidator.isValidEmoji("대한민국")).isFalse(); // 한글
        assertThat(EmojiValidator.isValidEmoji("")).isFalse(); // 빈 문자열
        assertThat(EmojiValidator.isValidEmoji(null)).isFalse(); // null
        assertThat(EmojiValidator.isValidEmoji(" ")).isFalse(); // 공백
    }

    @Test
    void testUtf8mb4Compatibility() {
        // utf8mb4 호환 이모지들
        assertThat(EmojiValidator.isUtf8mb4Compatible("🇰🇷")).isTrue();
        assertThat(EmojiValidator.isUtf8mb4Compatible("🇺🇸")).isTrue();
        assertThat(EmojiValidator.isUtf8mb4Compatible("😀")).isTrue();
        
        // 유효하지 않은 이모지
        assertThat(EmojiValidator.isUtf8mb4Compatible("KR")).isFalse();
    }

    @Test
    void testEmojiByteLength() {
        // 이모지의 바이트 길이 확인
        assertThat(EmojiValidator.getEmojiByteLength("🇰🇷")).isEqualTo(8); // 4바이트 + 4바이트
        assertThat(EmojiValidator.getEmojiByteLength("😀")).isEqualTo(4); // 4바이트
        assertThat(EmojiValidator.getEmojiByteLength("KR")).isEqualTo(2); // 1바이트 + 1바이트
        assertThat(EmojiValidator.getEmojiByteLength(null)).isEqualTo(0);
    }
} 