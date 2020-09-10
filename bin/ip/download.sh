ip_url=http://ftp.apnic.net/apnic/stats/apnic/delegated-apnic-latest
ip_txt=china_ip.txt
ip_txt_new=china_ip_new.txt
ip_backup_folder=backup
cur_date=$(date +'%Y%m%d%H%M%S')

# 删除上一个ip_txt_new文本
if [ -f ${ip_txt_new} ]; then
	rm -rf ${ip_txt_new}
fi

# 除第一个生成ip.txt 文本外，第二次更新，要先生成新的ip_new.txt，不能直接删除ip.text, 文本下载需要12min，以免此期间影响业务代码访问该文本
curl ${ip_url} | grep ipv4 | grep CN | awk -F\| '{ printf("%s/%d\n", $4, 32-log($5)/log(2)) }' > ${ip_txt_new}

echo 'download ip text complete'

echo 'start backup and replace'

# 生成ip_new.txt后, 加上日期后缀备份上一份ip.txt, 然后替换当前ip.txt为最新的 ip_new.txt
if [ ! -d ${ip_backup_folder} ]; then
	mkdir ${ip_backup_folder}
fi

if [ -f ${ip_txt} ]; then
	mv ${ip_txt} ./${ip_backup_folder}/${ip_txt}_${cur_date}
fi

if [ -f ${ip_txt_new} ]; then
	mv ${ip_txt_new} ${ip_txt}
fi

echo 'generate new ip text complete'
