#!/bin/bash

# 오디오 파일 업로드 및 URL 수집 스크립트
AUDIO_DIR="/Users/iyeonseo/Downloads/audios"
AUDIO_URLS=()

echo "오디오 파일 업로드 시작..."

# 첫 20개 파일 업로드
for i in {1..20}; do
    file="$AUDIO_DIR/audio$i.m4a"
    
    if [ -f "$file" ]; then
        echo "업로드 중: audio$i.m4a"
        
        # 파일 업로드하고 URL 받기
        response=$(curl -s -X POST "https://me-mory.mooo.com/upload?type=audio" \
            -H "Content-Type: multipart/form-data" \
            -F "file=@$file")
        
        if [ $? -eq 0 ]; then
            echo "✓ audio$i.m4a 업로드 완료"
            echo "URL: $response"
            AUDIO_URLS+=("$response")
        else
            echo "✗ audio$i.m4a 업로드 실패"
        fi
        
        sleep 0.5
    else
        echo "파일을 찾을 수 없습니다: $file"
    fi
done

echo "=========================================="
echo "업로드된 오디오 URL 목록:"
for i in "${!AUDIO_URLS[@]}"; do
    echo "audio$((i+1)): ${AUDIO_URLS[$i]}"
done

# URL들을 파일로 저장
echo "# 업로드된 오디오 URL 목록" > uploaded_audio_urls.txt
for i in "${!AUDIO_URLS[@]}"; do
    echo "${AUDIO_URLS[$i]}" >> uploaded_audio_urls.txt
done

echo "URL 목록이 uploaded_audio_urls.txt에 저장되었습니다."
