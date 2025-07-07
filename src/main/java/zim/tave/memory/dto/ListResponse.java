package zim.tave.memory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * 모든 리스트 응답을 {"data": [...]} 형태로 감싸기 위한 제네릭 DTO.
 *
 *
 * @param <T> 리스트의 요소 타입
 */
@Schema(description = "리스트 응답을 data 필드로 감싸는 제네릭 응답 DTO")
public class ListResponse<T> {
    @Schema(description = "실제 데이터 리스트")
    private List<T> data;

    public ListResponse(List<T> data) { this.data = data; }
    public List<T> getData() { return data; }
} 