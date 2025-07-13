#!/bin/bash

# 오디오 URL들을 저장할 배열
audio_urls=()

echo "=== 오디오 파일 업로드 시작 ==="

# 오디오 파일들 업로드
for i in {1..31}; do
    echo "Uploading audio${i}.m4a..."
    url=$(curl -s -X POST "https://me-mory.mooo.com/upload" -F "file=@/Users/iyeonseo/Downloads/audios/audio${i}.m4a" -F "type=audio")
    audio_urls+=("$url")
    echo "Uploaded: $url"
    sleep 0.5
done

echo "=== 모든 다이어리 ID 수집 ==="

# 모든 다이어리 ID 가져오기
diary_ids=($(curl -s -X GET "https://me-mory.mooo.com/api/diaries" | jq -r ".[].id"))

echo "Total diaries: ${#diary_ids[@]}"

# 31개 다이어리 ID를 랜덤으로 선택
selected_diary_ids=($(printf "%s
" "${diary_ids[@]}" | shuf | head -31))

echo "=== 다이어리에 오디오 URL 추가 시작 ==="

# 선택된 다이어리들에 오디오 URL 추가
for i in {0..30}; do
    diary_id=${selected_diary_ids[i]}
    audio_url=${audio_urls[i]}
    
    echo "Adding audio to diary ${diary_id}: ${audio_url}"
    
    curl -s -X PUT "https://me-mory.mooo.com/api/diaries/${diary_id}/optional-fields" \
         -H "Content-Type: application/json" \
         -d "{\"audioUrl\": \"${audio_url}\"}"
    
    echo " - Done"
    sleep 0.5
done

echo "=== 완료! ==="
echo "31개 오디오 파일이 업로드되고 31개 다이어리에 랜덤으로 추가되었습니다."
