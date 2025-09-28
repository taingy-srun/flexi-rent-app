# Network Setup for Real Device Testing

## Current Configuration

The app is now configured to connect to your development machine at:
- **API Base URL**: `http://192.168.0.182:8080/`
- **Your Machine's IP**: `192.168.0.182`

## Prerequisites for Real Device Testing

### 1. Same Network Connection
- **Development Machine**: Connected to WiFi network
- **Android Device**: Connected to the SAME WiFi network
- Both devices must be on the same subnet (192.168.0.x)

### 2. Firewall Configuration
Your development machine's firewall may block incoming connections. To allow access:

#### On macOS:
```bash
# Check if firewall is blocking connections
sudo pfctl -s rules | grep 8080

# Or temporarily disable firewall (NOT recommended for production)
sudo pfctl -d
```

#### Alternative - Allow specific port:
```bash
# Add firewall rule to allow port 8080
sudo pfctl -f /etc/pf.conf
```

### 3. Services Configuration
Ensure your room rental services are binding to all interfaces, not just localhost:

#### Check docker-compose.yml ports:
```yaml
ports:
  - "8080:8080"  # This binds to all interfaces (0.0.0.0:8080)
  # NOT: - "127.0.0.1:8080:8080"  # This would bind only to localhost
```

## Testing Connectivity

### From Your Development Machine:
```bash
# Test if API is accessible from your IP
curl http://192.168.0.182:8080/api/properties

# Check what's listening on port 8080
netstat -an | grep 8080
```

### From Your Android Device:
1. Open a web browser on your device
2. Navigate to: `http://192.168.0.182:8080/`
3. You should see a JSON error response (this means it's working)

## Troubleshooting

### If Connection Still Fails:

1. **Check Network:**
   ```bash
   # On development machine - check IP
   ifconfig | grep "inet "

   # Ping test from development machine to device
   # (You'll need your device's IP)
   ping [DEVICE_IP]
   ```

2. **Check Docker Container Binding:**
   ```bash
   # See which ports Docker is exposing
   docker ps

   # Should show: 0.0.0.0:8080->8080/tcp
   # NOT: 127.0.0.1:8080->8080/tcp
   ```

3. **Test Port Accessibility:**
   ```bash
   # Test if port 8080 is accessible from outside
   nc -l 8080  # On development machine
   # Then try connecting from device browser
   ```

4. **Alternative IP Discovery:**
   If the IP changed, find current IP:
   ```bash
   # macOS/Linux
   ifconfig | grep "inet " | grep -v 127.0.0.1

   # Or specifically for WiFi interface
   ifconfig en0 | grep "inet "
   ```

## Quick Fix If IP Changes

If your development machine's IP address changes:

1. **Find new IP:**
   ```bash
   ifconfig | grep "inet " | grep -v 127.0.0.1
   ```

2. **Update ApiClient.kt:**
   ```kotlin
   private const val BASE_URL = "http://[NEW_IP]:8080/"
   ```

3. **Update network_security_config.xml:**
   ```xml
   <domain includeSubdomains="true">[NEW_IP]</domain>
   ```

## Current Status

✅ **API Client**: Updated to use `192.168.0.182:8080`
✅ **Network Security**: Configured to allow cleartext traffic to your IP
✅ **API Accessibility**: Confirmed API responds on your IP address

The app should now be able to connect to your local API from a real device!