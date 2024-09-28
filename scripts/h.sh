#!/usr/bin/env bash
# echo "The total number of arguments is: $#"
# echo "All arguments except the first: ${@:2}"
arg="${arg:--j}"
location="${location:-get}"
data="${data:-}"
method="${method:-GET}"
auth="${auth:-}"
auth_type="${auth_type:-}"
session="${session:-}"


  while [ $# -gt 0 ]; do
    case "$1" in
      --session)
        session="$2"
        shift
        ;;
      --arg)
        arg="$2"
        shift
        ;;
      --location|-l)
        location="$2"
        shift
        ;;
      --auth|-a)
        auth="$2"
        shift
        ;;
      --auth-type|-A)
        auth_type="$2"
        shift
        ;;
      --data|-d)
        data="$2"
        shift
        ;;
      --method|-m)
        method="$2"
        shift
        ;;
      --*)
        echo "Illegal option $1"
        ;;
    esac
    shift $(( $# > 0 ? 1 : 0 ))
  done
  echo "################# ${method} ${location}"
  dynamic="";
  if [ "${auth}X" = "X" ]; then
    :
  else
    dynamic="${dynamic} --auth ${auth}"
  fi

  if [ "${auth_type}X" = "X" ]; then
    :
  else
    dynamic="${dynamic} --auth-type ${auth_type}"
  fi

  if [ "${session}X" = "X" ]; then
    :
  else
    dynamic="${dynamic} --session ${session}"
  fi

  tmp1_final="/tmp/springboot-httpbin.txt"
  tmp2_final="/tmp/httpbin.txt"



  http -v $arg $dynamic $method http://127.0.0.1:8080/"${location}" $data > ${tmp1_final}
  http -v $arg $dynamic $method https://httpbin.org:443/"${location}" $data > ${tmp2_final}

  diff -y ${tmp1_final} ${tmp2_final}
  echo ""
  echo "############################################################################"
