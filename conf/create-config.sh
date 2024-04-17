#!/bin/bash
# helper to generate configurations to run locally
# export STAGES=('ca10_papmcloud-prod_prod-prod')

# export APP_NAME='papm-jco-test-prod-dev22224';

export STAGES=('ca10_papmcloud-prod_prod-prod')

export APP_NAME='jco_demo_test2';

for t in "${STAGES[@]}"; do
  if [ "$CI" != 'true' ]; then
    source ./credentials.sh
  else 
    exit 1
  fi
  FIELD=$t
  IFS='_' read -ra ARRAY <<< "$FIELD"
  echo "--------------------------------------------------"
  echo "Region:   ${ARRAY[0]}"
  echo "Org:      ${ARRAY[1]}"
  echo "Space:    ${ARRAY[2]}"
  echo "--------------------------------------------------"

  export REGION="${ARRAY[0]}"
  export DOMAIN="cfapps.$REGION.hana.ondemand.com"
  export API="https://api.cf.$REGION.hana.ondemand.com/" 
  export ORG="${ARRAY[1]}"
  export SPACE="${ARRAY[2]}"
  TARGET_DIRECTORY="${SPACE}-${REGION}"
  echo "Target directory: ${TARGET_DIRECTORY}"
  mkdir "${TARGET_DIRECTORY}"

  cf login -a "$API" -o "$ORG" -s "$SPACE" -u "$CF_USER" -p "$CF_PASS"

  cf env "${APP_NAME}" | awk '/System-Provided/{ f = 1; next } /Staging Environment/{ f = 0 } f' > envVarsBroker.txt
  sed -i 's/: null,/: "",/g' envVarsBroker.txt
  sleep 2
  echo "DONE READING ENV VARS"
  input="envVarsBroker.txt"
  NEW_LINE=''
  USER_PROVIDED=''
  while IFS= read -r line
  do
    if [[ "${line: -1}" == ':' ]] ; then
      if [[ "${line}" == 'User-Provided:' ]] ; then
        USER_PROVIDED='X'
      fi
      continue
    fi 
    if [[ "$line" = '' ]] || [[ "$USER_PROVIDED" = 'X' ]]; then
      if [ -n "$NEW_LINE" ] || [ "$USER_PROVIDED" = 'X' ]; then
        if [ "$USER_PROVIDED" = 'X' ]; then
          NEW_LINE="$line" 
        fi
        FILE_NAME="./${TARGET_DIRECTORY}/.env"
        NEW_LINE=$(echo "$NEW_LINE" | sed -r 's/ //g')
        NEW_LINE=$(echo "$NEW_LINE" | sed -r 's/:/=/')
        echo "$NEW_LINE" >> "$FILE_NAME"
        NEW_LINE='';
      fi
      
    else
      NEW_LINE="$NEW_LINE""$line" 
    fi 
  done < "$input"
  echo "DONE"
  rm envVarsBroker.txt
done