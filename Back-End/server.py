from flask import Flask, request, jsonify

app = Flask(__name__)

# Simulated meter database (for testing)
meters = {
    "123456": {
        "balance": 0,
        "last_token": None
    }
}

# Hardcoded valid tokens (for demo)
valid_tokens = {
    "TOKEN-001": 50,
    "TOKEN-002": 100,
    "TOKEN-003": 75
}

@app.route('/api/token', methods=['POST'])
def process_token():
    data = request.get_json()

    meter_id = data.get('meter_id')
    token = data.get('token')

    if not meter_id or not token:
        return jsonify({"status": "error", "message": "Missing meter_id or token"}), 400

    if meter_id not in meters:
        return jsonify({"status": "error", "message": "Meter not found"}), 404

    if token not in valid_tokens:
        return jsonify({"status": "error", "message": "Invalid token"}), 400

    
    credit_amount = valid_tokens[token]
    meters[meter_id]["balance"] += credit_amount
    meters[meter_id]["last_token"] = token

    return jsonify({
        "status": "success",
        "message": f"Meter {meter_id} credited with {credit_amount} units.",
        "new_balance": meters[meter_id]["balance"]
    }), 200

@app.route('/api/meter/<meter_id>', methods=['GET'])
def get_meter_status(meter_id):
    if meter_id not in meters:
        return jsonify({"status": "error", "message": "Meter not found"}), 404

    return jsonify({
        "meter_id": meter_id,
        "balance": meters[meter_id]["balance"],
        "last_token": meters[meter_id]["last_token"]
    }), 200

if __name__ == '__main__':
    # PC's IP address
    app.run(host='192.168.8.197', port=5000)
    app.run(host="0.0.0.0", port=5000)
