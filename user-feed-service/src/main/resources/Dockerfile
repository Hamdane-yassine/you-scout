# Use the official Cassandra Docker image as the base
FROM cassandra:4.0.7

# Expose the required port for Cassandra
EXPOSE 9042

# Optional: Copy any additional configuration files or scripts
# COPY cassandra.yaml /etc/cassandra/

# Set any additional environment variables if needed
# ENV MY_VARIABLE=value

# Start the Cassandra service when the container launches
CMD ["cassandra", "-f"]
