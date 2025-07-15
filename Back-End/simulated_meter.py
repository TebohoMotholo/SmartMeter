# simulated_meter.py
import tkinter as tk
import requests

class SimulatedMeterBox(tk.Tk):
    def __init__(self, meter_id, server_url, poll_interval=2000):
        super().__init__()
        self.meter_id = meter_id
        self.server = server_url.rstrip('/')
        self.poll_interval = poll_interval

        
        self.title(f"Simulated Meter: {meter_id}")
        self.geometry("300x120")
        self.balance_var = tk.StringVar(value="Balance: …")
        self.token_var   = tk.StringVar(value="Last Token: …")

        tk.Label(self, textvariable=self.balance_var, font=("Arial", 16)).pack(pady=5)
        tk.Label(self, textvariable=self.token_var,   font=("Arial", 14)).pack(pady=5)

       
        self.after(500, self.refresh_status)

    def refresh_status(self):
        try:
            resp = requests.get(f"{self.server}/api/meter/{self.meter_id}")
            data = resp.json()
            self.balance_var.set(f"Balance: {data['balance']} Units")
            lt = data.get('last_token') or "N/A"
            self.token_var.set(f"Last Token: {lt}")
        except Exception as e:
            self.balance_var.set("Error fetching status")
            print("Meter polling error:", e)
        finally:
            
            self.after(self.poll_interval, self.refresh_status)

if __name__ == "__main__":
    
    METER_ID  = "123456"
    SERVER_URL = "http://192.168.8.197:5000"

    app = SimulatedMeterBox(METER_ID, SERVER_URL)
    app.mainloop()
