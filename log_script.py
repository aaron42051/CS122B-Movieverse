print "SINGLE INSTANCE: "

log_file = open("Project Logs/Single-Instance/HTTP_1_servlet_log.txt", 'r')
total_sum = 0
total_count = 0
for line in log_file:
    total_sum += int(line)
    total_count += 1
average = float(total_sum/total_count)
print "HTTP 1T servlet: " + str(float(average/1000000))
log_file.close()

log_file = open("Project Logs/Single-Instance/HTTP_1_jdbc_log.txt", 'r')
total_sum = 0
total_count = 0
for line in log_file:
    total_sum += int(line)
    total_count += 1
average = float(total_sum/total_count)
print "HTTP 1T jdbc: " + str(float(average/1000000))
log_file.close()

log_file = open("Project Logs/Single-Instance/HTTP_10_servlet_log.txt", 'r')
total_sum = 0
total_count = 0
for line in log_file:
    total_sum += int(line)
    total_count += 1
average = float(total_sum/total_count)
print "HTTP 10T servlet: " + str(float(average/1000000))
log_file.close()

log_file = open("Project Logs/Single-Instance/HTTP_10_jdbc_log.txt", 'r')
total_sum = 0
total_count = 0
for line in log_file:
    total_sum += int(line)
    total_count += 1
average = float(total_sum/total_count)
print "HTTP 10T jdbc: " + str(float(average/1000000))
log_file.close()

log_file = open("Project Logs/Single-Instance/HTTPS_10_servlet_log.txt", 'r')
total_sum = 0
total_count = 0
for line in log_file:
    total_sum += int(line)
    total_count += 1
average = float(total_sum/total_count)
print "HTTPS 10T servlet: " + str(float(average/1000000))
log_file.close()

log_file = open("Project Logs/Single-Instance/HTTPS_10_jdbc_log.txt", 'r')
total_sum = 0
total_count = 0
for line in log_file:
    total_sum += int(line)
    total_count += 1
average = float(total_sum/total_count)
print "HTTPS 10T jdbc: " + str(float(average/1000000))
log_file.close()

log_file = open("Project Logs/Single-Instance/HTTP_NoPrep_10_servlet_log.txt", 'r')
total_sum = 0
total_count = 0
for line in log_file:
    total_sum += int(line)
    total_count += 1
average = float(total_sum/total_count)
print "HTTP 10T NoPrep servlet: " + str(float(average/1000000))
log_file.close()

log_file = open("Project Logs/Single-Instance/HTTP_NoPrep_10_jdbc_log.txt", 'r')
total_sum = 0
total_count = 0
for line in log_file:
    total_sum += int(line)
    total_count += 1
average = float(total_sum/total_count)
print "HTTP 10T NoPrep jdbc: " + str(float(average/1000000))
log_file.close()

log_file = open("Project Logs/Single-Instance/HTTP_NoPool_10_servlet_log.txt", 'r')
total_sum = 0
total_count = 0
for line in log_file:
    total_sum += int(line)
    total_count += 1
average = float(total_sum/total_count)
print "HTTP 10T NoPool servlet: " + str(float(average/1000000))
log_file.close()

log_file = open("Project Logs/Single-Instance/HTTP_NoPool_10_jdbc_log.txt", 'r')
total_sum = 0
total_count = 0
for line in log_file:
    total_sum += int(line)
    total_count += 1
average = float(total_sum/total_count)
print "HTTP 10T NoPool jdbc: " + str(float(average/1000000))
log_file.close()

print ""
print "SCALED-INSTANCES: "

log_file = open("Project Logs/Scaled/HTTP_1_servlet_log.txt", 'r')
total_sum = 0
total_count = 0
for line in log_file:
    total_sum += int(line)
    total_count += 1
average = float(total_sum/total_count)
print "HTTP 1T servlet: " + str(float(average/1000000))
log_file.close()

log_file = open("Project Logs/Scaled/HTTP_1_jdbc_log.txt", 'r')
total_sum = 0
total_count = 0
for line in log_file:
    total_sum += int(line)
    total_count += 1
average = float(total_sum/total_count)
print "HTTP 1T jdbc: " + str(float(average/1000000))
log_file.close()

log_file = open("Project Logs/Scaled/HTTP_10_servlet_log.txt", 'r')
total_sum = 0
total_count = 0
for line in log_file:
    total_sum += int(line)
    total_count += 1
average = float(total_sum/total_count)
print "HTTP 10T servlet: " + str(float(average/1000000))
log_file.close()

log_file = open("Project Logs/Scaled/HTTP_10_jdbc_log.txt", 'r')
total_sum = 0
total_count = 0
for line in log_file:
    total_sum += int(line)
    total_count += 1
average = float(total_sum/total_count)
print "HTTP 10T jdbc: " + str(float(average/1000000))
log_file.close()

log_file = open("Project Logs/Scaled/HTTP_NoPrep_10_servlet_log.txt", 'r')
total_sum = 0
total_count = 0
for line in log_file:
    total_sum += int(line)
    total_count += 1
average = float(total_sum/total_count)
print "HTTP 10T NoPrep servlet: " + str(float(average/1000000))
log_file.close()

log_file = open("Project Logs/Scaled/HTTP_NoPrep_10_jdbc_log.txt", 'r')
total_sum = 0
total_count = 0
for line in log_file:
    total_sum += int(line)
    total_count += 1
average = float(total_sum/total_count)
print "HTTP 10T NoPrep jdbc: " + str(float(average/1000000))
log_file.close()

log_file = open("Project Logs/Scaled/HTTP_NoPool_10_servlet_log.txt", 'r')
total_sum = 0
total_count = 0
for line in log_file:
    total_sum += int(line)
    total_count += 1
average = float(total_sum/total_count)
print "HTTP 10T NoPool servlet: " + str(float(average/1000000))
log_file.close()

log_file = open("Project Logs/Scaled/HTTP_NoPool_10_jdbc_log.txt", 'r')
total_sum = 0
total_count = 0
for line in log_file:
    total_sum += int(line)
    total_count += 1
average = float(total_sum/total_count)
print "HTTP 10T NoPool jdbc: " + str(float(average/1000000))
log_file.close()

