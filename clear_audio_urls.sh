#!/bin/bash

# 오디오 URL이 있는 다이어리 ID들
diary_ids=(3 4 7 8 9 10 11 13 14 15 16 17 18 19 23 25 28 31 32 33)

echo "모든 다이어리의 오디오 URL을 null로 초기화 시작..."

for diary_id in "${diary_ids[@]}"; do
    echo "다이어리 ID $diary_id 오디오 URL 초기화 중..."
    
    curl -X PUT "https://me-mory.mooo.com/api/diaries/$diary_id/optional-fields" \
      -H "Content-Type: application/json" \
      -d '{"audioUrl": null}' \
      -s
    
    if [ $? -eq 0 ]; then
        echo "✓ 다이어리 ID $diary_id 완료"
    else
        echo "✗ 다이어리 ID $diary_id 실패"
    fi
    
    sleep 0.3
done

echo "모든 다이어리 오디오 URL 초기화 완료!"
