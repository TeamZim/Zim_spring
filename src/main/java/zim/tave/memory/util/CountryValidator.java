package zim.tave.memory.util;

import java.util.regex.Pattern;

public class CountryValidator {
    
    // ISO 3166-1 alpha-2 국가 코드 패턴 (2자리 대문자)
    private static final Pattern COUNTRY_CODE_PATTERN = Pattern.compile("^[A-Z]{2}$");
    
    /**
     * 국가 코드 형식 검증
     * @param countryCode 검증할 국가 코드
     * @return 유효한 형식이면 true, 아니면 false
     */
    public static boolean isValidCountryCodeFormat(String countryCode) {
        if (countryCode == null || countryCode.trim().isEmpty()) {
            return false;
        }
        return COUNTRY_CODE_PATTERN.matcher(countryCode.trim()).matches();
    }
    
    /**
     * 국가 코드 검증 (null, 빈 문자열, 형식 오류 체크)
     * @param countryCode 검증할 국가 코드
     * @throws IllegalArgumentException 유효하지 않은 국가 코드인 경우
     */
    public static void validateCountryCode(String countryCode) {
        if (countryCode == null || countryCode.trim().isEmpty()) {
            throw new IllegalArgumentException("국가 코드는 필수 입력 항목입니다.");
        }
        
        if (!isValidCountryCodeFormat(countryCode)) {
            throw new IllegalArgumentException("유효하지 않은 국가 코드 형식입니다: " + countryCode + " (ISO 3166-1 alpha-2 형식이어야 합니다)");
        }
    }
    
    /**
     * 검색 키워드 검증
     * @param keyword 검색할 키워드
     * @throws IllegalArgumentException 유효하지 않은 키워드인 경우
     */
    public static void validateSearchKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("검색 키워드는 필수 입력 항목입니다.");
        }
        
        if (keyword.trim().length() < 1) {
            throw new IllegalArgumentException("검색 키워드는 최소 1자 이상이어야 합니다.");
        }
        
        if (keyword.trim().length() > 50) {
            throw new IllegalArgumentException("검색 키워드는 최대 50자까지 입력 가능합니다.");
        }
    }
} 