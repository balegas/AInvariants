# -*- coding: utf-8 -*-
"""
Spyder Editor

This is a temporary script file.
"""
import numpy as np
import pandas as pd
import os


home="/Users/vbalegas/workspace/AInvariants/testData/"
os.chdir(home)

pivot = "client-state.log"
files = ["db-state-r11.log", "db-state-r12.log", "db-state-r13.log"]

df0 = pd.read_csv(pivot)
for filename in files:
    dfOther = pd.read_csv(filename)
    df0 = pd.merge_asof(df0, dfOther, on=['TS'], tolerance=1000);

print(df0)

    
    

